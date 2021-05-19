package com.springboot.bishe.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * ClassName: Role
 * Package: com.springboot.bishe.domain
 *
 * @Description:
 * @Date: 2021/4/1 16:04
 * @author: 浪漫
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ToString
@TableName("role")
public class Role implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private  String name;

    private  String remark;

    private  Integer available;

    private Date createtime;


}
