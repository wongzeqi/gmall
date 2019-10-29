package com.wjq.gmall.passport.controller;

import com.alibaba.fastjson.JSON;
import com.wjq.gmall.util.Const;
import com.wjq.gmall.util.HttpclientUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestAuth2 {
    @Test
    public String getCode() {

        //1 授权url
        String code = HttpclientUtil.doGet("https://api.weibo.com/oauth2/authorize?client_id="+Const.APP_KEY+"&response_type=code&redirect_uri="+ Const.OAUTH2_REDIRECT_URI);
        System.out.println(code);
        //获取 code 174af05fbdcd7d3ca85021cf42460eac

        //2 第二步 url  http://passport.gmall.com:8085/vlogin?code=174af05fbdcd7d3ca85021cf42460eac

        //3 通过上面的code去交换access_token
        return "";
    }




//    public static Map<String,String> getUser_info(){
//
//        // 4 用access_token查询用户信息
//        String s4 = "https://api.weibo.com/2/users/show.json?access_token=2.002f2mNGhZ8G2E37ee9d57930srDfr&uid=3477380424";
//        String user_json = HttpclientUtil.doGet(s4);
//        Map<String,String> user_map = JSON.parseObject(user_json,Map.class);
//
//        System.out.println(user_map);
//        for(String s:user_map.keySet()){
//            System.out.println("key : "+s+" value : "+user_map.get(s));
//        }
//
//        return user_map;
//    }


//    public static void main(String[] args) {
//
//        getUser_info();
////https://api.weibo.com/2/users/show.json
//    }
}
