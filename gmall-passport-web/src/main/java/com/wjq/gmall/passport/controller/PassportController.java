package com.wjq.gmall.passport.controller;


import com.wjq.gmall.annotations.LoginRequire;
import com.wjq.gmall.bean.UmsMember;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PassportController {
    @LoginRequire(loginSuccess = false)
    @RequestMapping("index")
    public String index(String ReturnUrl, ModelMap modelMap){
        if(StringUtils.isNotBlank(ReturnUrl)){
            modelMap.put("ReturnUrl",ReturnUrl);
        }
        return "index";
    }

    @RequestMapping("login")
    @ResponseBody
    public String login(UmsMember umsMember){
        String token = null;
        //调用用户服务 验证用户名密码
        return "token";
    }
    @RequestMapping("verify")
    @ResponseBody
    public String verify(UmsMember umsMember){
        String token = null;
        //调用用户服务 验证用户名密码
        return "success";
    }

}
