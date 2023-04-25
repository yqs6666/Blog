package com.yqs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yqs.pojo.Comment;

@SuppressWarnings({"all"})
public interface ICommentService extends IService<Comment> {
	Object getByBlogId(Long id);
	Boolean saveComment(Comment comment);
}
