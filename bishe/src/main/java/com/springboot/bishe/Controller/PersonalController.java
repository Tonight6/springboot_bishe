package com.springboot.bishe.Controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ClassName: PersonalController
 * Package: com.springboot.bishe.Controller
 *
 * @Description:
 * @Date: 2021/4/22 16:16
 * @author: 浪漫
 */
@Controller
@RequestMapping("personal")
public class PersonalController {


    @RequestMapping("toPersonal")
    public String toPersonal(){
        return "main/personalInformation.html";
    }


}
