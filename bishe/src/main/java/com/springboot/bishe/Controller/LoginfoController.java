package com.springboot.bishe.Controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.springboot.bishe.common.DataGridView;
import com.springboot.bishe.domain.Loginfo;
import com.springboot.bishe.service.LoginfoService;
import com.springboot.bishe.vo.LoginfoVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 *  登陆日志控制器
 */
@RestController
@RequestMapping("/loginfo")
public class LoginfoController {
	
	@Autowired
	private LoginfoService loginfoService;
	
	/**
	 * 全查询
	 */
	//@Cacheable注解适用于查询，@CachePut适用于修改和新增，@CacheEvict则适用于删除。
	@RequestMapping("loadAllLoginfo")
	@Cacheable(value = "Loginfo",keyGenerator = "keyGenerator")
	public DataGridView loadAllLoginfo(LoginfoVo loginfoVo) {
		IPage<Loginfo> page=new Page<>(loginfoVo.getPage(), loginfoVo.getLimit());
		QueryWrapper<Loginfo> queryWrapper=new QueryWrapper<>();
		queryWrapper.like(StringUtils.isNotBlank(loginfoVo.getUsername()),"loginname", loginfoVo.getUsername());
		queryWrapper.like(StringUtils.isNotBlank(loginfoVo.getLoginip()), "loginip",loginfoVo.getLoginip());
		queryWrapper.ge(loginfoVo.getStartTime()!=null, "logintime", loginfoVo.getStartTime());
		queryWrapper.le(loginfoVo.getEndTime()!=null, "logintime", loginfoVo.getEndTime());
		queryWrapper.orderByDesc("logintime");
		this.loginfoService.page(page, queryWrapper);
		return new DataGridView(page.getTotal(), page.getRecords());
	}
	
	
	/**
	 * 删除
	 */
	@RequestMapping("deleteLoginfo")
	public DataGridView deleteLoginfo(Integer id) {
		try {
			this.loginfoService.removeById(id);
			return DataGridView.success("删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return DataGridView.fail("删除失败");
		}
	}
	
	
	/**
	 * 批量删除
	 */
	@RequestMapping("batchDeleteLoginfo")
	public DataGridView batchDeleteLoginfo(LoginfoVo loginfoVo) {
		try {
			Collection<Serializable> idList=new ArrayList<Serializable>();
			for (Integer id : loginfoVo.getIds()) {
				idList.add(id);
			}
			this.loginfoService.removeByIds(idList);
			return DataGridView.success("删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return DataGridView.fail("删除失败");
		}
	}

}

