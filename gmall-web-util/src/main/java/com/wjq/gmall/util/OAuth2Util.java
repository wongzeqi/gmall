package com.wjq.gmall.util;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

public class OAuth2Util {
    // 授权码code换取access_token
    public static Map<String,String> getAccess_token(String code){
        //封装参数
        Map<String,String> paramMap = new HashMap<>();

        paramMap.put("client_id",Const.APP_KEY);
        paramMap.put("client_secret",Const.APP_SECRET);
        paramMap.put("grant_type",Const.GRENT_TYPE);
        paramMap.put("redirect_uri",Const.OAUTH2_REDIRECT_URI);

        paramMap.put("code",code);// 授权有效期内可以使用，没新生成一次授权码，说明用户对第三方数据进行重启授权，之前的access_token和授权码全部过期
        //发送请求获取请求数据
        String access_token_json = HttpclientUtil.doPost(Const.GET_ACCESS_TOKEN_API, paramMap);

        Map<String,String> access_map = JSON.parseObject(access_token_json,Map.class);

        //System.out.println(access_map.get("access_token"));

        //System.out.println(access_map.get("uid"));

        return access_map;
    }

    public static Map<String,Object> getUserInfo(String access_token,String uid){

        // 4 用access_token查询用户信息
        String url = String.format(Const.USER_INFO_API,access_token,uid);//拼接获取用户信息的url

        String user_json = HttpclientUtil.doGet(url);

        Map<String,Object> user_map = JSON.parseObject(user_json,Map.class);

        //System.out.println(user_map);

        for(String s:user_map.keySet()){

            System.out.println("key : "+s+" value : "+user_map.get(s));
        }

        return user_map;
    }


}
