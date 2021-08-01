package com.springboot.bishe.service.impl;

import com.springboot.bishe.domain.LogMethd;
import com.springboot.bishe.service.LogMethdService;
import org.springframework.stereotype.Service;

@Service
public class LogMethdServiceImpl implements LogMethdService {

    @Override
    public boolean save(LogMethd sysLog) {
        return true;
    }
}
