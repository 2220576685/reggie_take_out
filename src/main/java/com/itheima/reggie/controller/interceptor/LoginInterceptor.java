package com.itheima.reggie.controller.interceptor;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description:test
 * @Creatr by : jinyu
 * @Date: 2021/12/2 0002 10:14
 */

@Slf4j
@Component


public class LoginInterceptor implements HandlerInterceptor{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getSession().getAttribute("employee")!=null){
            log.info("用户名已登录,用户名id为:{}", request.getSession().getAttribute("employee"));

            //使用一用户一个相同进程，让数据可以从session所携带的id属性，被公共字段自动填充获得。
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            return true;
        }
        if (request.getSession().getAttribute("user")!=null){
            log.info("用户名已登录,用户名id为:{}", request.getSession().getAttribute("user"));

            //使用一用户一个相同进程，让数据可以从session所携带的id属性，被公共字段自动填充获得。
            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            return true;
        }
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return false;
    }
}