package com.yqs.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqs.exception.BlogNotFoundException;
import com.yqs.mapper.BlogMapper;
import com.yqs.pojo.Blog;
import com.yqs.pojo.Tag;
import com.yqs.pojo.User;
import com.yqs.service.IBlogService;
import com.yqs.service.ITagService;
import com.yqs.service.ITypeService;
import com.yqs.service.IUserService;
import com.yqs.util.MarkdownUtils;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"all"})
@Service
public class BlogserviceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {

	@Resource
	private ITypeService typeService;

	@Resource
	private ITagService tagService;

	@Resource
	private IUserService userService;

	@Override
	public boolean updateBlog(Blog blog) {
		Blog b = getById(blog.getId());
		if (b == null) {
			throw new BlogNotFoundException("该博客不存在！");
		}
		BeanUtil.copyProperties(blog, b);
		b.setUpdateTime(new Date());
		return updateById(b);
	}

	@Override
	public Page<Blog> listBlogBySearch(Page page, Blog blog, Long typeId) {
		QueryWrapper<Blog> wrapper = new QueryWrapper<>();
		QueryWrapper<Blog> queryWrapper = wrapper.like(!StringUtils.isEmpty(blog.getTitle()),"title", blog.getTitle())
				.eq(typeId != null,"type_id", typeId)
				.eq(true,"recommend", blog.isRecommend());
		return page(page, queryWrapper);
	}

	@Transactional
	@Override
	public boolean saveBlog(Blog blog) {
		blog.setCreateTime(new Date());
		blog.setUpdateTime(new Date());
		blog.setViews(0);
		blog.setType(typeService.getById(blog.getTypeId()));
		blog.setUserId(blog.getUser().getId());
		//blog.setTags(tagService.listByIds(Arrays.asList(blog.getTagIds())));
		return save(blog);
	}

	@Override
	public Page<Blog> pageBlog(Integer current, Integer size, boolean isAdmin) {
		List<Blog> blogs = list();
		blogs.sort((b1, b2) -> b2.getUpdateTime().compareTo(b1.getUpdateTime()));
		Page<Blog> page = new Page<>();
		page.setCurrent(current);
		page.setSize(size);
		page.setTotal(blogs.size());
		if (!isAdmin) {
			blogs = blogs.stream().filter(e -> e.isPublish()).collect(Collectors.toList());
		}
//			QueryWrapper<Blog> queryWrapper = new QueryWrapper<Blog>().eq("publish", true);
//			page = page(new Page<Blog>(current, size),queryWrapper);
//		} else {
//			page.setRecords(blogs);
////			page = page(new Page<Blog>(current, size));
//		}
		page.setRecords(
				blogs.subList(
						(current - 1) * size,
						(current * size) < blogs.size() ? (current * size) : blogs.size()));
		page.getRecords().stream().forEach(e -> e.setType(typeService.getById(e.getTypeId())));
//		page.getRecords().sort((b1, b2) -> b2.getUpdateTime().compareTo(b1.getUpdateTime()));
		page.getRecords().stream().forEach(e ->  {
			e.setUser(userService.getById(e.getUserId()));
		});
		return page;
	}

	@Override
	public void deleteBlog(Long id) {
		Blog blog = getById(id);
		if (blog == null) {
			throw new BlogNotFoundException("博客不存在或已被删除！");
		} else {
			removeById(id);
		}
	}

	@Override
	public Page<Blog> search(Integer current, Integer size, String query) {
		QueryWrapper<Blog> queryWrapper = new QueryWrapper<Blog>()
				.eq("publish", true)
				.and(b1 ->
						b1.like("title",query)
								.or()
								.like("content",query));
		Page<Blog> page = page(new Page<Blog>(current, size),queryWrapper);
		page.getRecords().stream().forEach(e -> e.setType(typeService.getById(e.getTypeId())));
		page.getRecords().sort((b1, b2) -> b2.getUpdateTime().compareTo(b1.getUpdateTime()));
		page.getRecords().stream().forEach(e ->  {
			e.setUser(userService.getById(e.getUserId()));
		});
		return page;
	}

	@Override
	public Blog getBlog(Long id, User user) {
		Blog blog = getById(id);
		blog.setUser(user);
		List<Integer> tagIds = Arrays.stream(blog.getTagIds().split(",")).map(Integer::parseInt).collect(Collectors.toList());
		blog.setTags(tagIds.stream().map(tag -> tagService.getById(tag)).collect(Collectors.toList()));
		return blog;
	}

	@Override
	public Blog getAndConvert(Long id, User user) {
		Blog blog = getById(id);
		blog.setUser(user);
		if (blog == null) {
			throw new BlogNotFoundException("该博客不存在");
		}
		boolean isSuccess = update(blog,
				new UpdateWrapper<Blog>()
						.eq("id",id)
						.set("views", blog.getViews() + 1));
		Blog b = new Blog();
		BeanUtils.copyProperties(blog,b);
		String content = b.getContent();
		List<Integer> tagIds = Arrays.stream(b.getTagIds().split(",")).map(Integer::parseInt).collect(Collectors.toList());
		b.setTags(tagIds.stream().map(tag -> tagService.getById(tag)).collect(Collectors.toList()));
		b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
		return b;
	}

	@Override
	public Page pageBlogByType(Integer current, int defaultPageSize, Long typeId) {
		Page<Blog> page;
		page = page(new Page<Blog>(current, defaultPageSize),
				new QueryWrapper<Blog>().eq("type_id", typeId));
		page.getRecords().stream().forEach(e -> e.setType(typeService.getById(e.getTypeId())));
		page.getRecords().sort((b1, b2) -> b2.getUpdateTime().compareTo(b1.getUpdateTime()));
		page.getRecords().stream().forEach(e ->  {
			e.setUser(userService.getById(e.getUserId()));
		});
		return page;
	}

	@Override
	public Page pageBlogByTags(Integer current, int defaultPageSize, Long id) {
		Page<Blog> page;
		page = page(new Page<Blog>(current, defaultPageSize),
				new QueryWrapper<Blog>().like("tag_ids", id));
		page.getRecords().stream().forEach(e -> e.setType(typeService.getById(e.getTypeId())));
		page.getRecords().stream()
				.forEach(e -> {
					List<Tag> tags = new ArrayList<>();
					Arrays.stream(e.getTagIds().split(",")).forEach(tagId -> {
						Tag t = tagService.getById(tagId);
						tags.add(t);
							}
					);
					e.setTags(tags);
				});
		page.getRecords().sort((b1, b2) -> b2.getUpdateTime().compareTo(b1.getUpdateTime()));
		page.getRecords().stream().forEach(e ->  {
			e.setUser(userService.getById(e.getUserId()));
		});
		return page;
	}
}
