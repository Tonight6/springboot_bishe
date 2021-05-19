package com.springboot.bishe.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.springboot.bishe.Mapper.NoticeMapper;
import com.springboot.bishe.domain.Notice;
import com.springboot.bishe.service.NoticeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 老雷
 * @since 2019-09-21
 */
@Service
@Transactional
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

}
