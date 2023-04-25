package com.yqs.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"all"})
@Data
@TableName("t_type")
public class Type implements Serializable {
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	@NotBlank(message = "分类名称不能为空")
	private String name;
	@TableField(exist = false)
	private List<Blog> blogs = new ArrayList<>();
}
