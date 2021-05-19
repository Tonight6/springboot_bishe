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
 *   @Data相当于@Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode这5个注解的合集,
 *   和@EqualsAndHashCode默认是false
 */
@Data
@EqualsAndHashCode(callSuper = false) //@EqualsAndHashCode(callSuper = true)，那就是用自己的属性和从父类继承的属性 来生成hashcode
@Accessors(chain = true)
@ToString
public class Customer implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String customername;

    private String zip;

    private String address;

    private String telephone;

    private String connectionperson;

    private String phone;

    private String bank;

    private String account;

    private String email;

    private String fax;

    private Integer available;

    
}
