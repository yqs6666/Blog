package com.yqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqs.mapper.UserMapper;
import com.yqs.pojo.User;
import com.yqs.service.IUserService;
import com.yqs.util.MD5Utils;
import org.springframework.stereotype.Service;

import javax.management.Query;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
	@Override
	public User checkUser(String email, String password) {
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.setEntity(new User().setEmail(email).setPassword(MD5Utils.code(password)));
		User user = getOne(queryWrapper);
		return user;
	}
}