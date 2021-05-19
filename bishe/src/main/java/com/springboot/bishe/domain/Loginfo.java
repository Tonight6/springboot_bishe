package com.springboot.bishe.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * ClassName: Loginfo
 * Package: com.springboot.bishe.domain
 *
 * @Description:
 * @Date: 2021/4/11 19:17
 * @author: 浪漫
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Loginfo {
    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String username;

    private String loginip;

    //@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date logintime;
}
