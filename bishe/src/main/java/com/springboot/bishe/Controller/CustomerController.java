package com.springboot.bishe.Controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.springboot.bishe.common.DataGridView;
import com.springboot.bishe.domain.Customer;
import com.springboot.bishe.service.CustomerService;
import com.springboot.bishe.vo.CustomerVo;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;


@RestController
@RequestMapping("/customer")
@Api(tags = "登陆认证模块")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	/**
	 * 查询
	 */
	@RequestMapping("loadAllCustomer")
	public DataGridView loadAllCustomer(CustomerVo customerVo) {
		IPage<Customer> page = new Page<>(customerVo.getPage(), customerVo.getLimit());
		QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
		queryWrapper.like(StringUtils.isNotBlank(customerVo.getCustomername()), "customername",
				customerVo.getCustomername());
		queryWrapper.like(StringUtils.isNotBlank(customerVo.getPhone()), "phone", customerVo.getPhone());
		queryWrapper.like(StringUtils.isNotBlank(customerVo.getConnectionperson()), "connectionperson",
				customerVo.getConnectionperson());
		this.customerService.page(page, queryWrapper);
		return new DataGridView(page.getTotal(), page.getRecords());
	}

	/**
	 * 添加
	 */
	@RequestMapping("addCustomer")

	public DataGridView addCustomer(CustomerVo customerVo) {
		try {
			this.customerService.save(customerVo);
			return DataGridView.fail("添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return DataGridView.fail("添加失败");
		}
	}

	/**
	 * 修改
	 */
	@RequestMapping("updateCustomer")
	public DataGridView updateCustomer(CustomerVo customerVo) {
		try {
			this.customerService.updateById(customerVo);
			return DataGridView.success("修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return DataGridView.fail("修改失败");
		}
	}

	/**
	 * 删除
	 */
	@RequestMapping("deleteCustomer")
	public DataGridView deleteCustomer(Integer id) {
		try {
			this.customerService.removeById(id);
			return DataGridView.success("删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return DataGridView.fail("删除失败");
		}
	}

	/**
	 * 批量删除
	 */
	@RequestMapping("batchDeleteCustomer")
	public DataGridView batchDeleteCustomer(CustomerVo customerVo) {
		try {
			Collection<Serializable> idList = new ArrayList<Serializable>();
			for (Integer id : customerVo.getIds()) {
				idList.add(id);
			}
			this.customerService.removeByIds(idList);
			return DataGridView.success("删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return DataGridView.fail("删除失败");
		}
	}
}
