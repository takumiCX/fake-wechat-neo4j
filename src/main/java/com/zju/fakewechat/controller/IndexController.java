package com.zju.fakewechat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: takumiCX
 * @create: 2019-01-18
 * @email: takumicx@zju.edu.cn
 **/
@Controller
public class IndexController {

    @RequestMapping("/")
    public String index(){
        return "index";
    }

}
