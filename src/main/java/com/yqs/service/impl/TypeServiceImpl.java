package com.yqs.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqs.exception.BlogNotFoundException;
import com.yqs.mapper.TypeMapper;
import com.yqs.pojo.Blog;
import com.yqs.pojo.Tag;
import com.yqs.pojo.Type;
import com.yqs.service.IBlogService;
import com.yqs.service.ITypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"all"})
@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type> implements ITypeService {

//	@Resource
//	private IBlogService blogService;

	@Transactional
	@Override
	public boolean saveType(Type type) {
		return save(type);
	}

	@Transactional
	@Override
	public Type getType(Long id) {
		return getById(id);
	}

	@Transactional
	@Override
	public Page<Type> listType(Page page) {
		return page(page);
	}

	@Transactional
	@Override
	public boolean updateType(Long id, Type type) {
		Type t = getById(id);
		if (t == null) {
			throw new BlogNotFoundException();
		}
		BeanUtil.copyProperties(type, t);
		return updateById(t);
	}

	@Transactional
	@Override
	public boolean deleteType(Long id) {
		return removeById(id);
	}

	@Override
	public List<Type> initTypes(List<Blog> blogs) {
		List<Type> types = list().stream().limit(6).collect(Collectors.toList());
		types.stream().forEach(type -> {
			List<Blog> blogsTarget = blogs.stream()
					.filter(blog -> blog.getTypeId() == type.getId())
					.collect(Collectors.toList());
			type.setBlogs(blogsTarget);
		});
		types.sort((t1, t2) -> Integer.compare(t2.getBlogs().size(), t1.getBlogs().size()));
//		types.stream().forEach(e -> {
//			List<Blog> blogs = blogService.query().eq("type_id", e.getId()).list();
//			e.setBlogs(blogs);
//		});
		return types;
	}

	@Override
	public List<Type> getTypes() {
		List<Type> types = list();
		return types;
	}
}
