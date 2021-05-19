package com.springboot.bishe.Controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot.bishe.common.Constast;
import com.springboot.bishe.common.DataGridView;
import com.springboot.bishe.common.TreeNode;
import com.springboot.bishe.domain.Permission;
import com.springboot.bishe.service.PermissionService;
import com.springboot.bishe.service.impl.PermissionServiceImpl;
import com.springboot.bishe.vo.PermissionVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName: PermissionController
 * Package: com.springboot.bishe.Controller
 *
 * @Description:
 * @Date: 2021/4/8 10:03
 * @author: 浪漫
 */
@RestController
@RequestMapping("permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;


    /**
     * 加载权限管理左边的权限树的json
     */
    @RequestMapping("loadPermissionManagerLeftTreeJson")
    public DataGridView loadPermissionManagerLeftTreeJson(PermissionVo permissionVo) {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", Constast.TYPE_MNEU);
        List<Permission> list = this.permissionService.list(queryWrapper);
        List<TreeNode> treeNodes = new ArrayList<>();
        for (Permission permission : list) {
            Boolean spread = permission.getOpen() == 1 ? true : false;
            treeNodes.add(new TreeNode(permission.getId(), permission.getPid(), permission.getTitle(), spread));
        }
        return new DataGridView(treeNodes);
    }


    /**
     * 查询右边的列表
     */
    @RequestMapping("loadAllPermission")
    public DataGridView loadAllPermission(PermissionVo permissionVo) {
        IPage<Permission> page = new Page<>(permissionVo.getPage(), permissionVo.getLimit());
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", Constast.TYPE_PERMISSION);// 只能查询权限
        queryWrapper.like(StringUtils.isNotBlank(permissionVo.getTitle()), "title", permissionVo.getTitle());
        queryWrapper.like(StringUtils.isNotBlank(permissionVo.getPercode()), "percode", permissionVo.getPercode());
        queryWrapper.eq(permissionVo.getId() != null,
                "pid", permissionVo.getId());
        queryWrapper.orderByAsc("ordernum");
        this.permissionService.page(page, queryWrapper);
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**************** 权限管理CRUD开始 ****************/

    /**
     * 添加时；加载最大的排序码
     *
     * @param
     * @return
     */
    @RequestMapping("loadPermissionMaxOrderNum")
    public Map<String, Object> loadPermissionMaxOrderNum() {
        Map<String, Object> map = new HashMap<String, Object>();

        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("ordernum");
        IPage<Permission> page = new Page<>(1, 1);
        List<Permission> list = this.permissionService.page(page, queryWrapper).getRecords();
        if (list.size() > 0) {
            map.put("value", list.get(0).getOrdernum() + 1);
        } else {
            map.put("value", 1);
        }
        return map;
    }

    /**
     * 添加
     *
     * @param permissionVo
     * @return
     */
    @RequestMapping("addPermission")
    public DataGridView addPermission(PermissionVo permissionVo) {
        try {
            permissionVo.setType(Constast.TYPE_PERMISSION);// 设置添加类型
            this.permissionService.save(permissionVo);
            return DataGridView.success("添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return DataGridView.fail("添加失败");
        }
    }

    /**
     * 修改
     *
     * @param permissionVo
     * @return
     */
    @RequestMapping("updatePermission")
    public DataGridView updatePermission(PermissionVo permissionVo) {
        try {
            this.permissionService.updateById(permissionVo);
            return DataGridView.success("修改成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return  DataGridView.fail("修改失败");
        }
    }

    /**
     * 删除
     *
     * @param permissionVo
     * @return
     */
    @RequestMapping("deletePermission")
    public DataGridView deletePermission(PermissionVo permissionVo) {
        try {
            this.permissionService.removeById(permissionVo.getId());
            return DataGridView.success("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return  DataGridView.fail("删除失败");
        }
    }


    /**************** 权限管理CRUD结束****************/
}
