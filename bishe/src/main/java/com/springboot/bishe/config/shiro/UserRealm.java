package com.springboot.bishe.config.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springboot.bishe.common.Constast;
import com.springboot.bishe.domain.Permission;
import com.springboot.bishe.domain.User;
import com.springboot.bishe.service.PermissionService;
import com.springboot.bishe.service.RoleService;
import com.springboot.bishe.service.UserService;
import com.springboot.bishe.vo.ActiverUser;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ClassName: UserRealm
 * Package: com.springboot.bishe.common.shiro
 *
 * @Description:
 * @Date: 2021/4/1 8:40
 * @author: 浪漫
 */
//  AuthenticatingRealm 只负责认证（登录）的Realm父类
public class UserRealm extends AuthorizingRealm {

    @Autowired
    @Lazy  //只有使用的时候才会加载
    private UserService userService;

    @Autowired
    @Lazy
    private PermissionService permissionService;

    @Autowired
    @Lazy
    private RoleService roleService;


    /**
     *  执行授权逻辑
     *  只要访问加上授权的资源都会调用改方法
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo=new SimpleAuthorizationInfo();
        ActiverUser activerUser=(ActiverUser) principalCollection.getPrimaryPrincipal();
        User user=activerUser.getUser();
        List<String> permissions = activerUser.getPermissions();
        System.out.println(permissions + "permissions=-=-==--------------------");
        if(user.getType()== Constast.USER_TYPE_SUPER) {
            authorizationInfo.addStringPermission("*:*");
        }else {
            if(null!=permissions&&permissions.size()>0) {
                authorizationInfo.addStringPermissions(permissions);
            }
        }
        System.out.println(authorizationInfo+"=-=-=--==-=-=-=-=-=-");
        return authorizationInfo;
    }

    /**
     *  执行认证逻辑
     *  只要使用subject.login(token) 就会调用该方法
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    /**
     * Shiro的认证方法我们需要在这个方法中来获取用户的信息（从数据库中）
     * @param authenticationToken   用户登录时的Token（令牌），这个对象中将存放着我们用户在浏览器中存放的账号和密码
     * @return 返回一个AuthenticationInfo 对象，这个返回以后Shiro会调用这个对象中的一些方法来完成对密码的验证 密码是由Shiro
     *         进行验证是否合法
     * @throws AuthenticationException   如果认证失败Shiro就会抛出AuthenticationException 我们也可以手动自己抛出这个AuthenticationException
     * 以及它的任意子异常类不通的异常类型可以认证过程中的不通错误情况我们需要根据异常类型来为用户返回特定的响应数据
     * AuthenticationException 异常的子类  可以我们自己抛出
     *      AccountException 账号异常  可以我们自己抛出
     *      UnknownAccountException 账号不存在的异常  可以我们自己抛出
     *      LockedAccountException  账号异常锁定异常  可以我们自己抛出
     *      IncorrectCredentialsException  密码错误异常 这个异常会在Shiro进行密码验证是抛出
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", authenticationToken.getPrincipal().toString());
        User user = userService.getOne(queryWrapper);
        if (null != user) {
            ActiverUser activerUser = new ActiverUser();
            activerUser.setUser(user);

            //根据用户ID查询percode
            //查询所有菜单
            QueryWrapper<Permission> qw=new QueryWrapper<>();
            //设置只能查询菜单
            qw.eq("type",Constast.TYPE_PERMISSION);
            qw.eq("available", Constast.AVAILABLE_TRUE);

            //根据用户ID+角色+权限去查询
            Integer userId=user.getId();
            //根据用户ID查询角色
            List<Integer> currentUserRoleIds = roleService.queryUserRoleIdsByUid(userId);
            //根据角色ID取到权限和菜单ID
            Set<Integer> pids=new HashSet<>();
            for (Integer rid : currentUserRoleIds) {
                List<Integer> permissionIds = roleService.queryRolePermissionIdsByRid(rid);
                pids.addAll(permissionIds);
            }
            List<Permission> list=new ArrayList<>();
            //根据角色ID查询权限
            if(pids.size()>0) {
                qw.in("id", pids);
                list=permissionService.list(qw);
            }
            List<String> percodes=new ArrayList<>();
            for (Permission permission : list) {
                percodes.add(permission.getPercode());
            }
            //放到
            activerUser.setPermissions(percodes);

            // 盐加密次数为1
            System.out.println(activerUser + "activerUser================");

            return new SimpleAuthenticationInfo(activerUser, user.getPassword(),
                    this.getName());
        }
        return null;
    }
}
