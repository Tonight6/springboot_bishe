package com.springboot.bishe.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springboot.bishe.Mapper.LoginfoMapper;
import com.springboot.bishe.domain.Loginfo;
import com.springboot.bishe.service.LoginfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class LoginfoServiceImpl extends ServiceImpl<LoginfoMapper, Loginfo> implements LoginfoService {

}
