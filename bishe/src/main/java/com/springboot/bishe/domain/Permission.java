package com.springboot.bishe.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * ClassName: piermission
 * Package: com.springboot.bishe.domain
 *
 * @Description:
 * @Date: 2021/4/1 16:20
 * @author: 浪漫
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ToString
@TableName("permission")
public class Permission  implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)

    private Integer id;

    /**
     *  父id
     */
    private Integer pid;

    /**
     * 权限类型[menu/permission]
     */
    private String type;

    private String title;

    /**
     * 权限标志[只有type= permission才有  user:view]
     */
    private String percode;

    /**
     *  图标
     */
    private String icon;


    private String href;

    private String target;

    private Integer open;

    /**
     *  排序
     */
    private Integer ordernum;

    /**
     * 状态【0不可用1可用】
     */
    private Integer available;
}
