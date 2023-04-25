package com.yqs.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqs.pojo.Blog;
import com.yqs.pojo.User;

@SuppressWarnings({"all"})
public interface IBlogService extends IService<Blog> {
	boolean updateBlog(Blog blog);
	Page<Blog> listBlogBySearch(Page page, Blog blog, Long typeId);

	boolean saveBlog(Blog blog);

	Page<Blog> pageBlog(Integer current, Integer size, boolean isAdmin);

	void deleteBlog(Long id);

	Page<Blog> search(Integer current, Integer size, String query);

	Blog getBlog(Long id, User user);

	Blog getAndConvert(Long id,User user);

	Page pageBlogByType(Integer current, int defaultPageSize, Long id);

	Page pageBlogByTags(Integer current, int defaultPageSize, Long id);
}
