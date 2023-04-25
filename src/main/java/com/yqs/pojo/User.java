package com.yqs.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings({"all"})
@Data
@TableName("t_user")
public class User implements Serializable {
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	private String nickname;
	private String username;
	private String password;
	private String email;
	private String avatar;
	private Integer type;
	private Date createTime;
	private Date updateTime;

	public User setId(Long id) {
		this.id = id;
		return this;
	}

	public User setNickname(String nickname) {
		this.nickname = nickname;
		return this;
	}

	public User setUsername(String username) {
		this.username = username;
		return this;
	}

	public User setPassword(String password) {
		this.password = password;
		return this;
	}

	public User setEmail(String email) {
		this.email = email;
		return this;
	}

	public User setAvatar(String avatar) {
		this.avatar = avatar;
		return this;
	}

	public User setType(Integer type) {
		this.type = type;
		return this;
	}

	public User setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public User setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}
}
