package com.toutiao.controller;

import com.toutiao.model.User;
import com.toutiao.service.ToutiaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by admin on 16-7-1.
 */

//@Controller
public class IndexController {

    //可以加Logger 然后在下班可以显示Logger的具体信息 可以输出在控制台下面
        private static final Logger logger = LoggerFactory.getLogger(IndexController.class );

    //此处就不用New 一个对象了 因为在Service 中 @Service 了
    @Autowired
    private ToutiaoService toutiaoService;

    @RequestMapping(path = {"/", "/index"})
    @ResponseBody
    public String index(HttpSession session) {
        //toutiaoService.say();
        logger.info("Visit Index ");

        return "hello zhangqi, " + session.getAttribute("msg")+"<br> say:"+toutiaoService.say();

    }

    //放的是端口下的路径{}里面为穿的参数，
    @RequestMapping(value = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    //@PathVariable 中的变量
    //@RequestParam 不是上面端口里面的参数
    public String profile(@PathVariable("groupId") String groupId,
                          @PathVariable("userId") int userId,
                          @RequestParam(value = "type", defaultValue = "1") int type,
                          @RequestParam(value = "key", defaultValue = "newcoder") String key) {
        return String.format("GID{%s},UID{%d},TYPE{%d},KEY{%s}", groupId, userId, type, key);
    }

    @RequestMapping(value = {"/vm"})
    //可以在templates中写参数Model的具体实现的位置的引用
    public String news(Model model) {
        //model里面简单的传值
        model.addAttribute("value1", "vv1");

        List<String> colors = Arrays.asList(new String[]{"Red", "Green", "Blue"});
        model.addAttribute("colors", colors);

        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < 4; ++i) {
            map.put(String.valueOf(i), String.valueOf(i * i));
        }
        model.addAttribute("map", map);

        model.addAttribute("User", new User("Jim"));

        //返回的是模板 而不是具体的值
        return "news";
    }

    // request 参数解析，cookie读取 http请求字段 文件上传
    @RequestMapping(value = {"/request"})
    @ResponseBody
    public String request(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpSession session) {
        StringBuilder sb = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            sb.append(name + ":" + request.getHeader(name) + "<br>");
        }
        for (Cookie cookie : request.getCookies()) {
            sb.append("Cookie");
            sb.append(cookie.getName());
            sb.append(":");
            sb.append(cookie.getValue());
            sb.append("<br>");
        }
        sb.append("getMethod:" + request.getMethod() + "<br>");
        sb.append("getPathInfo:" + request.getPathInfo() + "<br>");
        sb.append("getQueryString:" + request.getQueryString() + "<br>");
        sb.append("getRequestURI:" + request.getRequestURI() + "<br>");
        return sb.toString();

    }

    //页面内容返回 cookie下发 http字段设置 headers
    @RequestMapping(value = {"/response"})
    @ResponseBody
    public String response(@CookieValue(value = "newcoderid", defaultValue = "a") String nowcoderId,
                           @RequestParam(value = "key", defaultValue = "key") String key,
                           @RequestParam(value = "value", defaultValue = "value") String value,
                           HttpServletResponse response) {
        response.addCookie(new Cookie(key, value));
        response.addHeader(key, value);
        return "NowcoderId From Cookie:" + nowcoderId;
    }

    //临时性跳转302 和永久性跳转301
  /*  @RequestMapping("/redirect/{code}")
    public RedirectView redirect(@PathVariable("code") int code){
        RedirectView red = new RedirectView("/",true);
        if (code == 301){
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return red;
    }*/
    //另一种写法 简单写法
    @RequestMapping("/redirect/{code}")
    public String redirect(@PathVariable("code") int code,
                           HttpSession session) {
        //用户打开一次网页进行一次会话表示为一次session
        session.setAttribute("msg", "Jump from redirect.");
        return "redirect:/";
    }

//进入下面的页面编辑管理员的key值 值对了显示页面，错了显示错误的提示
    @RequestMapping("/admin")
    @ResponseBody
    public String admin(@RequestParam(value = "key", required = true) String key) {
        if ("admin".equals(key)) {
            return "hello admin";
        }
        throw new IllegalArgumentException("key 错误");
    }
    //自己定义错误异常的显示，显示的消息如上所示
    @ExceptionHandler
    @ResponseBody
    public String error(Exception e){
        return "error:"+e.getMessage();
    }

}
