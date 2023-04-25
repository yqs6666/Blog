package com.yqs.config;

import com.yqs.interceptor.LoginInterCeptor;
import com.yqs.interceptor.RefreshTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@SuppressWarnings({"all"})
@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		// token刷新的拦截器
		registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate))
				.addPathPatterns("/**").order(0);
		// 登录拦截器
		registry.addInterceptor(new LoginInterCeptor())
				.addPathPatterns("/admin/**")
				.excludePathPatterns("/admin","/admin/login").order(100);
	}
}
