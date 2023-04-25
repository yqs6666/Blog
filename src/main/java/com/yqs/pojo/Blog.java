package com.yqs.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings({"all"})
/**
 * id : 主键
 * title :  标题
 * content : 内容
 * firstPicture : 首图
 * flag : 标记
 * views : 浏览次数
 * appreciation : 赞赏
 * shareStatement : 转载声明
 * commentabled : 评论
 * publish : 发布
 * recommended : 推荐
 * createTime : 创建时间
 * updateTime : 更新时间
 */
@Data
@TableName("t_blog")
public class Blog implements Serializable {
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	private String title;
	private String content;
	private String firstPicture;
	private String flag;
	private String description;
	private Integer views;
	@NotBlank
	private boolean appreciation;
	@TableField(value = "share_statement")
	@NotBlank
	private boolean shareStatement;
	@NotBlank
	private boolean commentabled;
	@NotBlank
	private boolean publish;
	@NotBlank
	private boolean recommend;
	@TableField(value = "create_time")
	private Date createTime;
	@TableField(value = "update_time")
	private Date updateTime;

	@TableField(exist = false)
	private Type type;
	@TableField(exist = false)
	private User user;
	@TableField("type_id")
	private Long typeId;
	@TableField("user_id")
	private Long userId;
	@TableField(exist = false)
	private List<Comment> comments = new ArrayList<>();
	private String tagIds;
	@TableField(exist = false)
	private List<Tag> tags = new ArrayList<>();
}
