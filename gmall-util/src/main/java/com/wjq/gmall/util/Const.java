package com.wjq.gmall.util;

public class Const {
    public static final String VERIFY_URL = "http://passport.gmall.com:8085/verify?token=";


    public static String TRICKER_IP = "http://192.168.10.109";

    public static String VERIFY_REDIRECT_URL ="http://passport.gmall.com:8085/index?ReturnUrl=";

    public static String PRIVATE_KEY ="WJQ_MALL";

    //社交登录相关

    public static String OAUTH2_REDIRECT_URI = "http://passport.gmall.com:8085/vlogin";

    public static String APP_KEY = "3976370353";

    public static String APP_SECRET = "53cc883749eea221e4619815d1e11966";

    public static String GRENT_TYPE = "authorization_code";

    //微博API

    public static String GET_ACCESS_TOKEN_API = "https://api.weibo.com/oauth2/access_token?";

    public static String USER_INFO_API = "https://api.weibo.com/2/users/show.json?access_token=%s&uid=%s";

    //用户类型

    public static String WEIBO_USER_TYPE = "2";

    //性别
    public static final String FEMALE = "0";

    public static final String MALE = "1";

    public static void main(String[] args) {
        System.out.println(String.format(USER_INFO_API,"a","b"));
    }




}
