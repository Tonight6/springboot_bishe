package com.springboot.bishe.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.springboot.bishe.Mapper.CustomerMapper;
import com.springboot.bishe.domain.Customer;
import com.springboot.bishe.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;


@Service
@Transactional
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

	
	@Override
	public boolean save(Customer entity) {
		return super.save(entity);
	}
	
	@Override
	public boolean updateById(Customer entity) {
		return super.updateById(entity);
	}
	
	@Override
	public boolean removeById(Serializable id) {
		return super.removeById(id);
	}
	@Override
	public Customer getById(Serializable id) {
		return super.getById(id);
	}
	
	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		return super.removeByIds(idList);
	}
	
}
