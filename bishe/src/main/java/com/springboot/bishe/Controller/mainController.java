package com.springboot.bishe.Controller;

import com.springboot.bishe.vo.ActiverUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * ClassName: mainController
 * Package: com.springboot.bishe.Controller
 *
 * @Description:
 * @Date: 2021/3/31 15:00
 * @author: 浪漫
 */
@Controller
public class mainController {

    /**
     * 404页面
     */
    @GetMapping("/error/404")
    public String error_404() {
        return "error/404";
    }

    /**
     * 500页面
     */
    @GetMapping("/error/500")
    public String error_500() {
        return "error/500";
    }


    /**
     *  跳转登录
     * @return
     */
    @GetMapping("/")
    public String toLogin(){
        return "login";
    }
    @GetMapping("/login")
    public String toLogin2(){
        return "login";
    }

    /**
     *  登陆注册页面
     * @return
     */
    @GetMapping("register.html")
    public String register(){
        return "register";
    }



    /**
     *  跳转到主页
     * @return
     */
    @GetMapping("/index.html")
    public String toIndex(){
        return "main/index";
    }

    @RequestMapping("index")
    public String rm(Model model){
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() || subject.isRemembered()){
            ActiverUser activerUser=(ActiverUser) subject.getPrincipal();
            subject.getSession().setAttribute("user",activerUser.getUser());
            return "main/index";

        }else {
            model.addAttribute("msg", "请先登录");
            return "/";
        }
    }

    /**
     *  跳转到工作台
     * @return
     */
    @GetMapping("/toDeskManager")
    public String toDeskManager(){
        return "main/deskManager";
    }


    /**
     *  跳转到商品管理
     * @return
     */
    @GetMapping("/main/toGoodsManager")
    public String toGoodsManager(){
        return "base/goods/goodsManager";
    }

    /**
     *  跳转到供应商管理
     * @return
     */
    @GetMapping("/main/toProviderManager")
    public String toProviderManager(){
        return "base/provider/providerManager";
    }


    /**
     *  跳转到客户管理
     * @return
     */
    @GetMapping("/main/toCustomerManager")
    public String toCustomerManager(){
        return "base/customer/customerManager";
    }




    /**
     *  跳转到菜单管理
     * @return
     */
    @GetMapping("/main/toMenuManager")
    public String toMenuManager(){
        return "system/menu/menuManager";
    }
    /**
     *  跳转到菜单管理左边的树
     * @return
     */
    @GetMapping("/main/toMenuLeft")
    public String toMenuLeft(){
        return "system/menu/menuLeft";
    }
    /**
     *  跳转到菜单管理右边的列表
     * @return
     */
    @GetMapping("/main/toMenuRight")
    public String toMenuRight(){
        return "system/menu/menuRight";
    }


    /**
     *  跳转到权限管理
     * @return
     */
    @GetMapping("/main/toPermissionManager")
    public String toPermission(){
        return "system/permission/permissionManager";
    }

    /**
     *  跳转到权限管理左边的树
     * @return
     */
    @GetMapping("/main/toPermissionLeft")
    public String toPermissionLeft(){
        return "system/permission/permissionLeft";
    }

    /**
     *  跳转到权限管理右边的表格
     * @return
     */
    @GetMapping("/main/toPermissionRight")
    public String tpPermissionRight(){
        return "system/permission/permissionRight";
    }

    /**
     *  跳转到角色管理
     * @return
     */
    @GetMapping("/main/toRoleManager")
    public String toRoleManager(){
        return "system/role/roleManager";
    }

    /**
     *  跳转到用户管理
     * @return
     */
    @GetMapping("/main/toUserManager")
    public String toUserManager(){
        return "system/user/userManager";
    }


    /**
     *  跳转到登陆日志
     * @return
     */
    @GetMapping("/main/toLogLoginManager")
    public String toLogLoginManager(){
        return "other/loginfo/loginfoManager";
    }

    /**
     *  跳转到公告管理
     * @return
     */
    @GetMapping("/main/toNoticeManager")
    public String toNoticeManager(){
        return "other/notice/noticeManager";
    }


    /**
     *  跳转到缓存管理
     * @return
     */
    @GetMapping("/main/toCacheManager")
    public String toCacheManager(){
        return "other/cache/toCacheManager";
    }






}
