package com.toutiao.controller;

import com.toutiao.async.EventModel;
import com.toutiao.async.EventProducer;
import com.toutiao.async.EventType;
import com.toutiao.service.UserService;
import com.toutiao.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by admin on 16-7-7.
 */
@Controller
public class LoginController{
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class );

    @Autowired
    UserService userService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/reg/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
   public String reg (Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "rember",defaultValue = "0")int rememberme,
                      HttpServletResponse response){
        try {
            Map<String,Object> map = userService.register(username,password);
            if (map.containsKey("ticket")) {
                    Cookie  cookie = new Cookie("ticket",map.get("ticket").toString());
                    cookie.setPath("/");
                    if (rememberme > 0){
                        cookie.setMaxAge(3600*24*5);
                    }
                response.addCookie(cookie);
                return ToutiaoUtil.getJSONString(0, "注册成功");
            }else {
                return ToutiaoUtil.getJSONString(1, map);
            }

        }catch (Exception e){
            logger.error("注册异常"+ e.getMessage());
            return ToutiaoUtil.getJSONString(1,"注册异常");
        }
        //return "";
    }

    @RequestMapping(path = {"/login/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String login (Model model, @RequestParam("username") String username,
                       @RequestParam("password") String password,
                       @RequestParam(value = "rember",defaultValue = "0")int rememberme,
                         HttpServletResponse response ){
        try {
            Map<String,Object> map = userService.login(username,password);
            if (map.containsKey("ticket")) {
                Cookie  cookie = new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme > 0){
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                eventProducer.fireEvent(new EventModel(EventType.LOGIN)
                 .setActorId((int) map.get("userId"))
                 .setExt("username",username).setExt("email","zhangqi920503@163.com"));
                return ToutiaoUtil.getJSONString(0, "登陆成功");
            }else {
                return ToutiaoUtil.getJSONString(1, map);
            }

        }catch (Exception e){
            logger.error("登陆异常"+ e.getMessage());
            return ToutiaoUtil.getJSONString(1,"登陆异常");
        }
        //return "";
    }

    @RequestMapping(path = {"/logout/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout (@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/";
    }

}
