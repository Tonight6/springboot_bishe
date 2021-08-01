package com.springboot.bishe.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * ClassName: LogMethd
 * Package: com.springboot.bishe.domain
 *
 * @Description:
 * @Date: 2021/6/15 10:37
 * @author: 浪漫
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ToString
public class LogMethd {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 操作IP
     */
    private String ip;

    /**
     * 操作类型 1 操作记录 2异常记录
     */
    private Integer type;

    /**
     * 操作描述
     */
    private String description;

    /**
     * 请求方法
     */
    private String actionMethod;

    /**
     * 请求url
     */
    private String actionUrl;

    /**
     * 请求参数
     */
    private String params;


    /**
     * 类路径
     */
    private String classPath;


    /**
     * 开始时间
     *
     * LocalDateTime
     */
    private Long startTime;

    /**
     * 完成时间
     */
    private Long finishTime;

    /**
     * 消耗时间
     */
    private Long consumingTime;

    /**
     * 异常描述 e.getMessage
     */
    private String exDesc;
}
