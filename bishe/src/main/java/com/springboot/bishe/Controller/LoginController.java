package com.springboot.bishe.Controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springboot.bishe.Mapper.UserMapper;
import com.springboot.bishe.common.DataGridView;
import com.springboot.bishe.domain.Loginfo;
import com.springboot.bishe.domain.User;
import com.springboot.bishe.service.LoginfoService;
import com.springboot.bishe.service.UserService;
import com.springboot.bishe.service.impl.UserServiceImpl;
import com.springboot.bishe.vo.ActiverUser;
import com.springboot.bishe.vo.UserVo;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.wf.captcha.utils.CaptchaUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName: LoginController
 * Package: com.springboot.bishe.Controller
 *
 * @Description:
 * @Date: 2021/3/31 15:13
 * @author: 浪漫
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginfoService loginfoService;

    @Autowired
    private UserService userService;

    @RequestMapping("getCode")
    public void getCode(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
//        //定义图形验证码的长和宽
//        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(120,45,4,5);
//        session.setAttribute("code",lineCaptcha.getCode());
//        ServletOutputStream outputStream = response.getOutputStream();
//        ImageIO.write(lineCaptcha.getImage(),"JPEG",outputStream);
        // 三个参数分别为宽、高、位数
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        // 设置类型，纯数字、纯字母、字母数字混合
        specCaptcha.setCharType(Captcha.TYPE_DEFAULT);
        session.setAttribute("captcha", specCaptcha.text().toLowerCase());
       // CaptchaUtil.out(request, response);
        // 输出图片流
        specCaptcha.out(response.getOutputStream());

    }



    /**
     *  登录校验
     * @param username
     * @param password
     * @param captcha
     * @param request
     * @return
     */
    @PostMapping("login")
    @ResponseBody
    public DataGridView verifyLogin(String username,String password,String captcha,boolean remember,HttpServletRequest request) {
        DataGridView dataGridView = new DataGridView();
        System.out.println(captcha);
        if (captcha == null || !request.getSession().getAttribute("captcha").equals(captcha.trim().toLowerCase())) {
            dataGridView.setCode(-1);
            dataGridView.setMsg("验证码错误");
            return dataGridView;
        }

            // 获取当前用户
            Subject subject = SecurityUtils.getSubject();
            SimpleHash simpleHash=new SimpleHash("MD5", password, ByteSource.Util.bytes(username), 1);

            //封装用户登录数据
            UsernamePasswordToken token = new UsernamePasswordToken(username, simpleHash.toString());
            System.out.println(token.getPassword());
            if(remember){
                token.setRememberMe(true);
            }
            try {
                // 登录
                subject.login(token);
                ActiverUser activerUser=(ActiverUser) subject.getPrincipal();
                activerUser.getUser().getRealname();
                //session中存入用户信息
                subject.getSession().setAttribute("user",activerUser.getUser());
                //记录登陆日志
                Loginfo entity=new Loginfo();
                entity.setUsername(activerUser.getUser().getUsername()+"-"+activerUser.getUser().getRealname());
                entity.setLoginip(request.getRemoteAddr());// 获取ip
                entity.setLogintime(new Date());
                loginfoService.save(entity);


                dataGridView.setCode(200);
                dataGridView.setData("/index.html");
                return dataGridView;
            } catch (AuthenticationException e) {
                e.printStackTrace();
                dataGridView.setCode(-1);
                dataGridView.setMsg("登陆失败,用户名或密码不正确");
                return dataGridView;
            }

    }


    /**
     *  退出登录
     */
    @RequestMapping("login.html")
    public String loginout(Model model){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        model.addAttribute("msg","安全退出！");
        return "login";
    }


    /**
     *  用户注册
     */
    @RequestMapping("register")
    @ResponseBody
    public DataGridView register(UserVo userVo){
        System.out.println("进入注册");
        try{
        userVo.setCreatetime(new Date());
        userVo.setAvailable(1);
        userVo.setStatus(1);
        userVo.setType(1);
        userVo.setUsername("guest");

        SimpleHash simpleHash=new SimpleHash("MD5", userVo.getPassword(), ByteSource.Util.bytes(userVo.getRealname()), 1);
        userVo.setPassword(simpleHash.toString());
        boolean sign =  userService.save(userVo);

        if (sign){
            return DataGridView.success("注册成功！");
        }
        }catch (Exception e){
            e.printStackTrace();
        }

        return DataGridView.success("注册失败！");
    }





}
