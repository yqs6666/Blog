package com.yqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqs.mapper.CommentMapper;
import com.yqs.pojo.Comment;
import com.yqs.service.ICommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings({"all"})
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

	@Override
	public Object getByBlogId(Long id) {
		//父级评论
		List<Comment> comments = query().eq("blog_id", id).eq("parent_comment_id",-1).list();
		comments.sort((c1,c2) -> c2.getCreateTime().compareTo(c1.getCreateTime()));
		//comments.sort((c1,c2) -> c1.getCreateTime().compareTo(c2.getCreateTime()));
		comments.stream().forEach(comment -> {
			comment.setReplyComments(
							query().eq("parent_comment_id", comment.getId())
									.list());
//			comment.setParentComment(getById(comment.getParentCommentId()));
//			if (comment.getParentComment() != null) {
//				comment.setParentNickname(comment.getParentComment().getNickname());
//			}
			comment.getReplyComments().stream().forEach(replyComment -> {
				replyComment.setParentNickname(comment.getNickname());
				//replyComment.setParentComment(comment);
			});
		});
		return eachComment(comments);
	}

	@Override
	public Boolean saveComment(Comment comment) {
		Long parentCommentId = comment.getParentCommentId();
		if (parentCommentId != -1) {
			comment.setParentComment(getById(parentCommentId));
		} else {
			comment.setParentComment(null);
		}
		comment.setCreateTime(new Date());
		return save(comment);
	}

	/**
	 * 循环每个顶级的评论节点
	 * @param comments
	 * @return
	 */
	private List<Comment> eachComment(List<Comment> comments) {
		List<Comment> commentsView = new ArrayList<>();
		for (Comment comment : comments) {
			Comment c = new Comment();
			BeanUtils.copyProperties(comment,c);
			commentsView.add(c);
		}
		//合并评论的各层子代到第一级子代集合中
		combineChildren(commentsView);
		return commentsView;
	}

	/**
	 *
	 * @param comments root根节点，blog不为空的对象集合
	 * @return
	 */
	private void combineChildren(List<Comment> comments) {

		for (Comment comment : comments) {
			List<Comment> replys1 = comment.getReplyComments();
			for(Comment reply1 : replys1) {
				//循环迭代，找出子代，存放在tempReplys中
				recursively(reply1);
			}
			//修改顶级节点的reply集合为迭代处理后的集合
			comment.setReplyComments(tempReplys);
			//清除临时存放区
			tempReplys = new ArrayList<>();
		}
	}

	//存放迭代找出的所有子代的集合
	private List<Comment> tempReplys = new ArrayList<>();
	/**
	 * 递归迭代，剥洋葱
	 * @param comment 被迭代的对象
	 * @return
	 */
	private void recursively(Comment comment) {
		tempReplys.add(comment);//顶节点添加到临时存放集合
		comment.setReplyComments(
				query().eq("parent_comment_id", comment.getId())
						.list());
		comment.getReplyComments().stream().forEach(replyComment -> {
			replyComment.setParentNickname(comment.getNickname());
		});
		if (comment.getReplyComments().size()>0) {
			List<Comment> replys = comment.getReplyComments();
			for (Comment reply : replys) {
				tempReplys.add(reply);
				if (reply.getReplyComments().size()>0) {
					recursively(reply);
				}
			}
		}
	}
}
