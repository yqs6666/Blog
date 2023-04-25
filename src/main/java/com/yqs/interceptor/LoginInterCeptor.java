package com.yqs.interceptor;

import com.yqs.util.UserHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings({"all"})
public class LoginInterCeptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request,
	                         HttpServletResponse response,
	                         Object handler) throws Exception {
//		if (request.getSession().getAttribute("user") == null) {
//			response.sendRedirect("/admin");
//			return false;
//		}
		if (UserHolder.getUser() == null) {
			//未登录，重定向至登陆页面
			response.sendRedirect("/admin");
			return false;
		}
		return true;
	}
}
