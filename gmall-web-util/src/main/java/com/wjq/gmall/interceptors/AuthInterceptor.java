package com.wjq.gmall.interceptors;

import com.alibaba.fastjson.JSON;
import com.wjq.gmall.annotations.LoginRequire;
import com.wjq.gmall.util.Const;
import com.wjq.gmall.util.CookieUtil;
import com.wjq.gmall.util.HttpclientUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //判断被拦截的请求的方法 是否是需要拦截的方法
        HandlerMethod methodHandle = (HandlerMethod)handler;
        LoginRequire methodAnnotation = methodHandle.getMethodAnnotation(LoginRequire.class);

        if(methodAnnotation==null){
            //不需要拦截
            return true;
        }
        //需要拦截  获取该请求是否登陆成功
        boolean loginSuccess = methodAnnotation.loginSuccess();
        String token = "";
        String oldToken = CookieUtil.getCookieValue(request,"oldToken",true);
        if(StringUtils.isNotBlank(oldToken)){
            token = oldToken;
        }
        String newToken = request.getParameter("token");
        if(StringUtils.isNotBlank(newToken)){
            token = newToken;
        }
        String success = "fail";
        Map<String,String> successMap = new HashMap<>();
        //验证   提到外面来  调用认证中心进行认证  进行远程调用  通过rest风格的http请求进行 验证
        if(StringUtils.isNotBlank(token)) {

            //获取当前请求的ip地址
            String ip = request.getHeader("x-forwarded-for");// 通过nginx转发的客户端ip
            if(StringUtils.isBlank(ip)){
                ip = request.getRemoteAddr();// 从request中获取ip
                if(StringUtils.isBlank(ip)){
                    ip = "127.0.0.1";
                }
            }
            //获取解密后的信息  memberID nickname 等
            String successJson = HttpclientUtil.doGet(Const.VERIFY_URL + token +"&currentIp="+ip);//在这里调用了verify方法
            successMap = JSON.parseObject(successJson, Map.class);
            success = successMap.get("status");
        }

        if(loginSuccess){//必须登录才能使用

            if(!success.equals("success")){
                //重定向到passport登录
                StringBuffer requestUrl = request.getRequestURL();//获取请求路径
                response.sendRedirect(Const.VERIFY_REDIRECT_URL+requestUrl);
                return false;
            }
            //验证通过需要覆盖cookie
            if(StringUtils.isNotBlank(token)){
                CookieUtil.setCookie(request,response,"oldToken",token,60*60*2,true);
                System.out.println("cookie添加成功");
            }
            //需要将token携带的用户信息写入redis
        }else {
            // 没有登录也能用，但是必须验证
            if (success.equals("success")) {
                // 需要将token携带的用户信息写入
                request.setAttribute("memberId", successMap.get("memberId"));
                request.setAttribute("nickname", successMap.get("nickname"));

                //验证通过，覆盖cookie中的token
                if (StringUtils.isNotBlank(token)) {
                    CookieUtil.setCookie(request, response, "oldToken", token, 60 * 60 * 2, true);
                }

            }
        }

        return true;
    }
}
