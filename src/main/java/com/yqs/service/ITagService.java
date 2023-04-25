package com.yqs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yqs.pojo.Blog;
import com.yqs.pojo.Tag;

import java.util.List;

@SuppressWarnings({"all"})
public interface ITagService extends IService<Tag> {
	List<Tag> initTags(List<Blog> blogs);

	List<Tag> getTags();
}
