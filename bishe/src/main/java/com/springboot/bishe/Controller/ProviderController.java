package com.springboot.bishe.Controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot.bishe.common.Constast;
import com.springboot.bishe.common.DataGridView;
import com.springboot.bishe.domain.Provider;
import com.springboot.bishe.service.ProviderService;
import com.springboot.bishe.vo.ProviderVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/provider")
public class ProviderController {

	@Autowired
	private ProviderService providerService;

	/**
	 * 查询
	 */
	@RequestMapping("loadAllProvider")
	public DataGridView loadAllProvider(ProviderVo providerVo) {
		IPage<Provider> page = new Page<>(providerVo.getPage(), providerVo.getLimit());
		QueryWrapper<Provider> queryWrapper = new QueryWrapper<>();
		queryWrapper.like(StringUtils.isNotBlank(providerVo.getProvidername()), "providername",
				providerVo.getProvidername());
		queryWrapper.like(StringUtils.isNotBlank(providerVo.getPhone()), "phone", providerVo.getPhone());
		queryWrapper.like(StringUtils.isNotBlank(providerVo.getConnectionperson()), "connectionperson",
				providerVo.getConnectionperson());
		this.providerService.page(page, queryWrapper);
		return new DataGridView(page.getTotal(), page.getRecords());
	}

	/**
	 * 添加
	 */
	@RequestMapping("addProvider")
	public DataGridView addProvider(ProviderVo providerVo) {
		try {
			this.providerService.save(providerVo);
			return DataGridView.success("添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return DataGridView.success("添加失败");
		}
	}

	/**
	 * 修改
	 */
	@RequestMapping("updateProvider")
	public DataGridView updateProvider(ProviderVo providerVo) {
		try {
			this.providerService.updateById(providerVo);
			return DataGridView.success("修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return DataGridView.success("修改失败");
		}
	}

	/**
	 * 删除
	 */
	@RequestMapping("deleteProvider")
	public DataGridView deleteProvider(Integer id) {
		try {
			this.providerService.removeById(id);
			return DataGridView.success("删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return DataGridView.fail("删除失败");
		}
	}

	/**
	 * 批量删除
	 */
	@RequestMapping("batchDeleteProvider")
	public DataGridView batchDeleteProvider(ProviderVo providerVo) {
		try {
			Collection<Serializable> idList = new ArrayList<Serializable>();
			for (Integer id : providerVo.getIds()) {
				idList.add(id);
			}
			this.providerService.removeByIds(idList);
			return DataGridView.success("");
		} catch (Exception e) {
			e.printStackTrace();
			return DataGridView.fail("");
		}
	}
	
	
	/**
	 * 加载所有可用的供应商
	 */
	@RequestMapping("loadAllProviderForSelect")
	public DataGridView loadAllProviderForSelect() {
		QueryWrapper<Provider> queryWrapper=new QueryWrapper<>();
		queryWrapper.eq("available", Constast.AVAILABLE_TRUE);
		List<Provider> list = this.providerService.list(queryWrapper);
		return new DataGridView(list);
	}
}

