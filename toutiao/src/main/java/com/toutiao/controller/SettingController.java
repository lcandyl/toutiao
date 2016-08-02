package com.toutiao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by admin on 16-7-2.
 */
@Controller
public class SettingController {
    @RequestMapping("/setting")
    @ResponseBody
    public String Setting(){
        return "Setting : OK";
    }
}
