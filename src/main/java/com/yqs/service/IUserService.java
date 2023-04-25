package com.yqs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yqs.pojo.User;

public interface IUserService extends IService<User> {
	User checkUser(String username, String password);
}
