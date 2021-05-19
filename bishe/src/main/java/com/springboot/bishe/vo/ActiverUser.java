package com.springboot.bishe.vo;

import com.springboot.bishe.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * ClassName: ActiverUser
 * Package: com.springboot.bishe.vo
 *
 * @Description:
 * @Date: 2021/4/13 8:42
 * @author: 浪漫
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiverUser implements Serializable {

    private User user;

    private List<String> roles;

    private List<String> permissions;
}
