package com.springboot.bishe.Controller;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonToken;
import com.springboot.bishe.Mapper.UserMapper;
import com.springboot.bishe.common.Constast;
import com.springboot.bishe.common.DataGridView;
import com.springboot.bishe.common.UUIDUtil;
import com.springboot.bishe.domain.Role;
import com.springboot.bishe.domain.User;
import com.springboot.bishe.service.RoleService;
import com.springboot.bishe.service.UserService;
import com.springboot.bishe.vo.UserVo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * ClassName: UserController
 * Package: com.springboot.bishe.Controller
 *
 * @Description:
 * @Date: 2021/4/7 14:45
 * @author: 浪漫
 */

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;



    /**
     * 用户全查询
     */
    @RequestMapping("loadAllUser")
    public DataGridView loadAllUser(UserVo userVo) {
        System.out.println("用户全查询");

        IPage<User> page=new Page<>(userVo.getPage(), userVo.getLimit());
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
     ///   queryWrapper.eq(StringUtils.isNotBlank(userVo.getRealname()), "realname", userVo.getRealname()).or().eq(StringUtils.isNotBlank(userVo.getUsername()), "username", userVo.getUsername());
        // queryWrapper.eq(StringUtils.isNotBlank(userVo.getEmail()), "email", userVo.getEmail());
        queryWrapper.eq("type", Constast.USER_TYPE_NORMAL);//查询系统用户
        queryWrapper.orderByAsc("ordernum");
        List<User> list = this.userService.page(page, queryWrapper).getRecords();

        return new DataGridView(page.getTotal(), list);

    }

    /**
     *  用户修改密码
     * @param id
     * @return
     */
    @RequestMapping("resetPwd")
    public  DataGridView resetPwd(Integer id){
        try{
            User user = new User();
            user.setId(id);
            user.setPassword(Constast.USER_DEFAULT_PWD);
            this.userService.updateById(user);

            return  DataGridView.success("修改成功！");
        }catch (Exception e){
            e.getMessage();

            return DataGridView.fail("修改失败！");
        }
    }


    /**
     * 加载最大的排序码
     * @return
     */
    @RequestMapping("loadUserMaxOrderNum")
    public Map<String,Object> loadUserMaxOrderNum(){
        Map<String, Object> map=new HashMap<String, Object>();

        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("ordernum");
        IPage<User> page=new Page<>(1, 1);
        List<User> list = this.userService.page(page, queryWrapper).getRecords();
        if(list.size()>0) {
            map.put("value", list.get(0).getOrdernum()+1);
        }else {
            map.put("value", 1);
        }
        return map;
    }

    /**
     *  添加用户
     * @param userVo
     * @return
     */
    @RequestMapping("addUser")
    public DataGridView addUser(UserVo userVo){
        try {
            userVo.setCreatetime(new Date());
            userVo.setType(Constast.USER_TYPE_NORMAL);//设置类型
            userVo.setPassword(Constast.USER_DEFAULT_PWD);//设置密码
            this.userService.save(userVo);
            return DataGridView.success("添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return DataGridView.fail("添加失败！");
        }

    }

    /**
     *  修改用户
     * @param userVo
     * @return
     */
    @RequestMapping("updateUser")
    public DataGridView updateUser(UserVo userVo){
        try {
           boolean b  =   this.userService.updateById(userVo);
            System.out.println(b);
            return DataGridView.success("修改成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return DataGridView.fail("修改失败！");
        }
    }

    /**
     *  删除用户
     * @param id
     * @return
     */
    @RequestMapping("deleteUser")
    public DataGridView deleteUser(Integer id){
        try {
            this.userService.removeById(id);
            return DataGridView.success("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return DataGridView.fail("删除失败！");
        }


    }


    /**
     * 根据用户ID查询角色并选中已拥有的角色
     */
    @RequestMapping("initRoleByUserId")
    public DataGridView initRoleByUserId(Integer id) {
        //1,查询所有可用的角色
        QueryWrapper<Role> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("available", Constast.AVAILABLE_TRUE);
        List<Map<String, Object>> listMaps = this.roleService.listMaps(queryWrapper);

        //2,查询当前用户拥有的角色ID集合
        List<Integer> currentUserRoleIds=this.roleService.queryUserRoleIdsByUid(id);
        for (Map<String, Object> map : listMaps) {
            Boolean LAY_CHECKED=false;
            Integer roleId=(Integer) map.get("id");
            for (Integer rid : currentUserRoleIds) {
                if(rid==roleId) {
                    LAY_CHECKED=true;
                    break;
                }
            }
            map.put("LAY_CHECKED", LAY_CHECKED);
        }
        return new DataGridView(Long.valueOf(listMaps.size()), listMaps);
    }


    /**
     * 保存用户和角色的关系
     */
    @RequestMapping("saveUserRole")
    public DataGridView saveUserRole(Integer uid,Integer[] ids) {
        try {
            this.userService.saveUserRole(uid,ids);
            return DataGridView.success("保存成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return DataGridView.fail("保存失败！");
        }

    }




}
