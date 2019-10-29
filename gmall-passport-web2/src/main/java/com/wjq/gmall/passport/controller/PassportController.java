package com.wjq.gmall.passport.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.wjq.gmall.annotations.LoginRequire;
import com.wjq.gmall.bean.UmsMember;
import com.wjq.gmall.service.UserService;
import com.wjq.gmall.util.Const;
import com.wjq.gmall.util.CookieUtil;
import com.wjq.gmall.util.JwtUtil;

import com.wjq.gmall.util.OAuth2Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PassportController {

    @Reference
    UserService userService;

    @LoginRequire(loginSuccess = false)
    @RequestMapping("index")
    public String index(String ReturnUrl, ModelMap map){
        map.put("ReturnUrl",ReturnUrl);
        return "index";
    }

    @RequestMapping("login")
    @ResponseBody
    public String login(UmsMember umsMember, HttpServletRequest request, HttpServletResponse response){
        //调用用户服务 验证用户名密码
        UmsMember umsMemberLogin = userService.login(umsMember);
        //制作token
        String token = getTokenAndAddCookie(umsMemberLogin,request,response);
        //登录成功将token信息写入cookie中
        return token;
    }



    //这个方法并不是有用户调用的  而是有拦截器调用的  所有用户在请求 某一个服务后 有拦截器拦截请求 获取请求的ip地址
    //再由拦截器请求verify方法的时候将本次请求的ip （currentIp传递过来 就可以进行解密 进行验证）

    @RequestMapping("verify")
    @ResponseBody
    public String verify(String token,String currentIp){
        //通过jwt验证token的真假
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> decode = JwtUtil.decode(token, Const.PRIVATE_KEY,currentIp);
        if(decode!=null){
            map.put("status","success");
            map.put("memberId",(String)decode.get("memberId"));
            map.put("nickname",(String)decode.get("nickname"));
        }else {
            map.put("status","fail");
        }
        return JSON.toJSONString(map);
    }


    @RequestMapping("vlogin")

    public String vlogin(String code,HttpServletRequest request, HttpServletResponse response){

        //授权码换取access_token  //保存access_token
        Map<String,String> token_map = OAuth2Util.getAccess_token(code);
        String access_token = token_map.get("access_token");
        String uid = token_map.get("uid");

        //access_token 换取 用户信息
        Map<String,Object> user_map = OAuth2Util.getUserInfo(access_token,uid);
        //将用户信息保存到数据库   用户类型设置为微博用户
        String gender = Const.FEMALE;
        UmsMember umsMember = new UmsMember();
        umsMember.setAccessCode(code);
        umsMember.setSourceType(Const.WEIBO_USER_TYPE);
        umsMember.setSourceUid((String)user_map.get("idstr"));
        umsMember.setCity((String)user_map.get("location"));
        umsMember.setAccessToken(access_token);
        umsMember.setUsername((String)user_map.get("name"));
        umsMember.setNickname((String)user_map.get("name"));
        if(!user_map.get("gender").equals("m")){
            gender=Const.MALE;
        }
        umsMember.setGender(gender);
        //从数据库中查出来的数据
        UmsMember memberCheck = userService.checkOAuthUserBySourceId(umsMember.getSourceUid());
        //此用户不存在才进行保存操作
        if(memberCheck==null){
            userService.addOauthUser(umsMember);
        }

        String token = getTokenAndAddCookie(umsMember,request,response);
        //存在的话进行  登录授权   //生成jwt token 并且重定向到首页 携带该token


        return "redirect:http://search.gmall.com:8083/index?token="+token;
    }



    private String getTokenAndAddCookie(UmsMember member,HttpServletRequest request,HttpServletResponse response){
        String token = "fail";
        if(member!=null){
            //登录成功
            String memberId = member.getId();
            String nickname = member.getNickname();
            Map<String,Object> userMap = new HashMap<>();
            userMap.put("memberId",memberId);
            userMap.put("nickname",nickname);
            String ip = request.getHeader("x-forwarder-for");
            if(StringUtils.isBlank(ip)){
                ip = request.getRemoteAddr();
                if(StringUtils.isBlank(ip)){
                    ip = "127.0.0.1";
                }
            }

            token = JwtUtil.encode(Const.PRIVATE_KEY,userMap,ip);

            userService.addUserToken(token,memberId);//将token写入redis中
            CookieUtil.setCookie(request, response, "oldToken", token, 60 * 60 * 2, true);
        }
        return token;
    }

}
