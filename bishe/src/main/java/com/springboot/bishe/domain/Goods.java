package com.springboot.bishe.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * ClassName: Goods
 * Package: com.springboot.bishe.domain
 *
 * @Description:
 * @Date: 2021/4/17 18:05
 * @author: 浪漫
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ToString
@TableName("")
public class Goods implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // 商品名称
    private String goodsname;

    //生产地
    private String produceplace;

    private String size;

    //商品包装
    private String goodspackage;

    private String productcode;

    // 生产批号
    private String promitcode;

    private String description;

    private Double price;

    private Integer number;

    // 预警库存
    private Integer dangernum;

    private String goodsimg;

    private Integer available;

    //供应商id
    private Integer providerid;


    //供应商名称
    //@TableField(exist=false)
    // 表示当前属性不是数据库的字段，但在项目中必须使用，
    // 这样在新增等使用bean的时候，mybatis-plus就会忽略这个属性且报错
    @TableField(exist=false)
    private String providername;

}
