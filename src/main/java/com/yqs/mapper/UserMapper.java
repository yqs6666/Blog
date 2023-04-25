package com.yqs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yqs.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@SuppressWarnings({"all"})
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
