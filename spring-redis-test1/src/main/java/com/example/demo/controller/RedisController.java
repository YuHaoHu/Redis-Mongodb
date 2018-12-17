package com.example.demo.controller;


import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedisController
 * @Description
 * @Author Yuhao Hu
 * @Date 2018/12/17 9:18
 * @Version
 **/

@Controller
public class RedisController {
    @Autowired
    private StringRedisTemplate template;
    
    @RequestMapping(value="/login",method = RequestMethod.GET)
    public String login(){
    
        System.out.println("232323");
        return "login";
    }
    //生成验证码，并存入redis中，并设置过期时间，过期后自动注销
    @RequestMapping("/smsa/{phone}")
    public String  sms(Model model, HttpSession session, @PathVariable String phone){
		
		//java自带
        //生成6位数的验证码
        String code = RandomStringUtils.randomNumeric(6);
		
        //判断当前的redis中的key的size为多少
        if (template.opsForList().size(phone)<3){
            //当前缓存中size如果小于3的话，重新发送验证码
            template.opsForList().leftPush(phone, code);
            //设置key的过期时间为60秒
            template.expire(phone, 600, TimeUnit.SECONDS);
            
            model.addAttribute("send","已发送");
            model.addAttribute("sms",code);
            session.setAttribute("smsm",code);
            return "login";
        }else {
            model.addAttribute("fail","次数访问过多");
            return "login";
        }
    }
    
    //当验证码正确后
    @RequestMapping("/index/{num}")
    public String index(@PathVariable String num, HttpSession session, Model model){
        String smss =(String) session.getAttribute("smsm");
        if (smss.equals(num)){
            return "index";
        }else {
            model.addAttribute("no","验证码不正确...");
            return "login";
        }
    }
}
