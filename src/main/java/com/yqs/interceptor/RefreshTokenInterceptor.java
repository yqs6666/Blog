package com.yqs.interceptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.yqs.dto.UserDTO;
import com.yqs.util.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.yqs.util.RedisConstans.LOGIN_USER_KEY;
import static com.yqs.util.RedisConstans.LOGIN_USER_TTL;

@SuppressWarnings({"all"})
public class RefreshTokenInterceptor implements HandlerInterceptor {

	private StringRedisTemplate stringRedisTemplate;

	public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
		this.stringRedisTemplate = stringRedisTemplate;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 1.获取session / 请求头中的token
		//String token = request.getHeader("authorization");
		String token = (String) request.getSession().getAttribute("token");
		if (StrUtil.isBlank(token)) {
			return true;
		}
		// 2.获取session中的用户 / 基于token获取redis中的用户
		String key = LOGIN_USER_KEY + token;
		Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(key);
		// 3.判断用户是否存在
		if (userMap.isEmpty()) {
			return true;
		}
		// 5.将查询到的Hash数据转存为UserDto对象
		UserDTO userDTO = BeanUtil.fillBeanWithMap(userMap, new UserDTO(), false);
		// 6.存在，保存用户信息到ThreadLocal
		UserHolder.saveUser(userDTO);
		// 7.刷新token有效期
		stringRedisTemplate.expire(key,LOGIN_USER_TTL, TimeUnit.MINUTES);
		request.getSession().setMaxInactiveInterval(30 * 60);
		// 8.放行
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
}
