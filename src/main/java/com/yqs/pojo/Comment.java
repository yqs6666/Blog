package com.yqs.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings({"all"})
@Data
@TableName("t_comment")
public class Comment implements Serializable {
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	private String nickname;
	private String email;
	private String content;
	private String avatar;
	private Date createTime;
	private Long blogId;
	private Long parentCommentId;
	private Boolean adminComment;

	//回复评论
	@TableField(exist = false)
	private List<Comment> replyComments = new ArrayList<>();
	@TableField(exist = false)
	private Comment parentComment;
	@TableField(exist = false)
	private String parentNickname;
}
