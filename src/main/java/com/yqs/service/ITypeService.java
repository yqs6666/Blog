package com.yqs.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqs.pojo.Blog;
import com.yqs.pojo.Type;

import java.util.List;

@SuppressWarnings({"all"})
public interface ITypeService extends IService<Type> {

	boolean saveType(Type type);

	Type getType(Long id);

	Page<Type> listType(Page<Type> type);

	boolean updateType(Long id, Type type);

	boolean deleteType(Long id);

	List<Type> initTypes(List<Blog> blogs);

	List<Type> getTypes();
}
