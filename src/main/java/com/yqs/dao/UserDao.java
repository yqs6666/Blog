package com.yqs.dao;

import com.yqs.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@SuppressWarnings({"all"})
@Mapper
@Repository
public interface UserDao {
	User selectById(@Param("id") Long id);
}
