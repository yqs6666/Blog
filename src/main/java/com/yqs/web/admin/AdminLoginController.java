package com.yqs.web.admin;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import com.yqs.dto.UserDTO;
import com.yqs.pojo.User;
import com.yqs.service.IUserService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.yqs.util.RedisConstans.LOGIN_USER_KEY;
import static com.yqs.util.RedisConstans.LOGIN_USER_TTL;

@SuppressWarnings({"all"})
@Controller
@RequestMapping("/admin")
public class AdminLoginController {

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	@Resource
	private IUserService userService;

	@GetMapping("/login")
	public String login() {
		return "admin/login";
	}

	@GetMapping()
	public String loginPage() {
		return "admin/login";
	}

	@PostMapping("/login")
	public String login(@RequestParam String username,
	                    @RequestParam String password,
	                    HttpServletRequest request,
	                    HttpSession session,
	                    RedirectAttributes attributes) {
		User user = userService.checkUser(username, password);
		if (user != null) {
			//登陆成功
			//随机生成token，作为登录令牌
			String token = UUID.randomUUID(true).toString();
			//将user对象转为hash存储
			UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
			Map<String, Object> userMap = BeanUtil.beanToMap(userDTO,new HashMap<>(),
					CopyOptions.create().setIgnoreNullValue(true)
							.setFieldValueEditor((fieldName,fieldValue) -> fieldValue.toString()));
			//存储
			String tokenKey = LOGIN_USER_KEY + token;
			stringRedisTemplate.opsForHash().putAll(tokenKey,userMap);
			stringRedisTemplate.expire(tokenKey,LOGIN_USER_TTL, TimeUnit.MINUTES);
			user.setPassword(null);
			session.setAttribute("user",user);
			session.setAttribute("token",token);

			return "admin/index";
		}
		attributes.addFlashAttribute("message","用户名或密码错误！");
		return "redirect:/admin";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("user");
		return "redirect:/admin";
	}
}
