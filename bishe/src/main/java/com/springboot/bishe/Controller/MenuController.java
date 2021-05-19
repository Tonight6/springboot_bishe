package com.springboot.bishe.Controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot.bishe.common.Constast;
import com.springboot.bishe.common.DataGridView;
import com.springboot.bishe.common.TreeNode;
import com.springboot.bishe.common.TreeNodeBuilder;
import com.springboot.bishe.domain.Permission;
import com.springboot.bishe.domain.User;
import com.springboot.bishe.service.PermissionService;
import com.springboot.bishe.service.RoleService;
import com.springboot.bishe.vo.PermissionVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * ClassName: MenuController
 * Package: com.springboot.bishe.Controller
 *
 * @Description:
 * @Date: 2021/4/5 19:31
 * @author: 浪漫
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleService roleService;


    /**
     *  加载工作台左侧的菜单
     * @param permissionVo
     * @param session
     * @return
     */
    @RequestMapping("loadIndexLeftMenuJson")
    public DataGridView loadIndexLeftMenuJson(PermissionVo permissionVo, HttpSession session) {
        //查询所有菜单
        QueryWrapper<Permission> queryWrapper=new QueryWrapper<>();
        //设置只能查询菜单
        queryWrapper.eq("type", Constast.TYPE_MNEU);
        queryWrapper.eq("available", Constast.AVAILABLE_TRUE);


        User user = (User) session.getAttribute("user");
        List<Permission> list=null;

        if(user.getType() == Constast.USER_TYPE_SUPER) {
            list = permissionService.list(queryWrapper);
        }else {
            //根据用户ID+角色+权限去查询
            Integer userId=user.getId();
            //根据用户ID查询角色ID
            List<Integer> currentUserRoleIds = roleService.queryUserRoleIdsByUid(userId);
            //根据角色ID取到权限和菜单ID
            Set<Integer> pids=new HashSet<>();
            for (Integer rid : currentUserRoleIds) {
                List<Integer> permissionIds = roleService.queryRolePermissionIdsByRid(rid);
                pids.addAll(permissionIds);
            }

            //根据角色ID查询权限
            if(pids.size()>0) {
                queryWrapper.in("id", pids);
                list=permissionService.list(queryWrapper);
            }else {
                list =new ArrayList<>();
            }
        }
        List<TreeNode> treeNodes=new ArrayList<>();
        for (Permission p : list) {
            Integer id=p.getId();
            Integer pid=p.getPid();
            String title=p.getTitle();
            String icon=p.getIcon();
            String href=p.getHref();
            Boolean spread=p.getOpen()==Constast.OPEN_TRUE?true:false;
            treeNodes.add(new TreeNode(id, pid, title, icon, href, spread));
        }
        //构造层级关系
        List<TreeNode> list2 = TreeNodeBuilder.build(treeNodes, 1);
        return new DataGridView(list2);
    }



    /**
     * 加载菜单管理左边的菜单树的json
     */
    @RequestMapping("loadMenuManagerLeftTreeJson")
    public DataGridView loadMenuManagerLeftTreeJson(PermissionVo permissionVo) {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", Constast.TYPE_MNEU);
        List<Permission> list = this.permissionService.list(queryWrapper);
        List<TreeNode> treeNodes=new ArrayList<>();
        for (Permission menu : list) {
            Boolean spread=menu.getOpen()==1?true:false;
            treeNodes.add(new TreeNode(menu.getId(), menu.getPid(), menu.getTitle(), spread));
        }
        return new DataGridView(treeNodes);
    }

    /**
     * 查询
     */
    @RequestMapping("loadAllMenu")
    public DataGridView loadAllMenu(PermissionVo permissionVo) {
        IPage<Permission> page=new Page<>(permissionVo.getPage(), permissionVo.getLimit());
        QueryWrapper<Permission> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq(permissionVo.getId()!=null, "id", permissionVo.getId()).or().eq(permissionVo.getId()!=null,"pid", permissionVo.getId());
        queryWrapper.eq("type", Constast.TYPE_MNEU);//只能查询菜单
        queryWrapper.like(StringUtils.isNotBlank(permissionVo.getTitle()), "title", permissionVo.getTitle());
        queryWrapper.orderByAsc("ordernum");
        this.permissionService.page(page, queryWrapper);
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 加载最大的排序码
     * @param
     * @return
     */
    @RequestMapping("loadMenuMaxOrderNum")
    public Map<String,Object> loadMenuMaxOrderNum(){
        Map<String, Object> map=new HashMap<String, Object>();

        QueryWrapper<Permission> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("ordernum");
        IPage<Permission> page=new Page<>(1, 1);
        List<Permission> list = this.permissionService.page(page, queryWrapper).getRecords();
        if(list.size()>0) {
            map.put("value", list.get(0).getOrdernum()+1);
        }else {
            map.put("value", 1);
        }
        return map;
    }


    /**
     * 添加
     * @param permissionVo
     * @return
     */
    @RequestMapping("addMenu")
    public DataGridView addMenu(PermissionVo permissionVo) {
        try {
            permissionVo.setType(Constast.TYPE_MNEU);//设置添加类型
            this.permissionService.save(permissionVo);
            return DataGridView.success("添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return DataGridView.fail("添加失败");
        }
    }


    /**
     * 修改
     * @param permissionVo
     * @return
     */
    @RequestMapping("updateMenu")
    public DataGridView updateMenu(PermissionVo permissionVo) {
        try {
            this.permissionService.updateById(permissionVo);
            return DataGridView.success("修改成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return  DataGridView.fail("修改失败");
        }
    }


    /**
     * 查询当前的ID的菜单有没有子菜单
     */
    @RequestMapping("checkMenuHasChildrenNode")
    public Map<String,Object> checkMenuHasChildrenNode(PermissionVo permissionVo){
        Map<String, Object> map=new HashMap<String, Object>();

        QueryWrapper<Permission> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("pid", permissionVo.getId());
        List<Permission> list = this.permissionService.list(queryWrapper);
        if(list.size()>0) {
            map.put("value", true);
        }else {
            map.put("value", false);
        }
        return map;
    }

    /**
     * 删除
     * @param permissionVo
     * @return
     */
    @RequestMapping("deleteMenu")
    public DataGridView deleteMenu(PermissionVo permissionVo) {
        try {
            this.permissionService.removeById(permissionVo.getId());
            return DataGridView.success("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return  DataGridView.fail("删除失败");
        }
    }
}
