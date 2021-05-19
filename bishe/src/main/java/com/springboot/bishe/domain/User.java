package com.springboot.bishe.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * ClassName: User
 * Package: com.springboot.bishe.domain
 *
 * @Description:
 * @Date: 2021/4/1 15:11
 * @author: 浪漫
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ToString
@TableName("user")
public class User implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private  Integer  id;
    /**
     *  登录用户名
     */
    private  String username;

    private  String realname;

    private  String password;

    private  Integer sex;

    private String email;

    /**
     *  创建时间
     */

    private Date createtime;

    /**
     * 头像地址
     */
    private String uimage;


    /**
     *  排序
     */
    private Integer ordernum;

    /**
     * 用户类型[0，管理员1，普通用户]
     */
    private Integer type;

    /**
     *  用户状态。  0表示不在,1表示在线
     */
    private Integer status;



    /**
     *  是否可用    0表示不可用，1表示可用
     */
    private Integer available;

}
