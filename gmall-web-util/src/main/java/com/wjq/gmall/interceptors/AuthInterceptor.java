package com.wjq.gmall.interceptors;

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
        //验证   提到外面来  调用认证中心进行认证  进行远程调用  通过rest风格的http请求进行 验证
        if(StringUtils.isNotBlank(token)) {
            success = HttpclientUtil.doGet(Const.VERIFY_URL + token);
        }

        if(loginSuccess){//必须登录才能使用

            if(!success.equals("success")){
                //重定向到passport登录
                StringBuffer requestUrl = request.getRequestURL();//获取请求路径
                response.sendRedirect(Const.VERIFY_Redirect_URL+requestUrl);
                return false;
            }
            //验证通过需要覆盖cookie
            //需要将token携带的用户信息写入redis
            request.setAttribute("memberId","1");
            request.setAttribute("nickname","nickname");

        }else{//不需要登录也可以使用 ，但是必须验证
            if(success.equals("success")){
                //需要将token携带的用户信息写入redis
                request.setAttribute("memberId","1");
                request.setAttribute("nickname","nickname");
            }
        }
        if(StringUtils.isNotBlank(token)) {
            CookieUtil.setCookie(request, response, "oldToken", token, 60 * 60 * 2, true);
        }
        return true;
    }
}
