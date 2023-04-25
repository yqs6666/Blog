package com.yqs.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqs.mapper.TagMapper;
import com.yqs.pojo.Blog;
import com.yqs.pojo.Tag;
import com.yqs.service.IBlogService;
import com.yqs.service.ITagService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings({"all"})
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

//	@Resource
//	private IBlogService blogService;

	private List<Long> convertToList(String ids) {
		List<Long> list = new ArrayList<>();
		if (!"".equals(ids) && ids != null) {
			String[] idarray = ids.split(",");
			for (int i=0; i < idarray.length;i++) {
				list.add(new Long(idarray[i]));
			}
		}
		return list;
	}

	@Override
	public List<Tag> initTags(List<Blog> blogs) {
		List<Tag> tags = list().stream().limit(8).collect(Collectors.toList());
		tags.stream().forEach(tag -> {
			List<Blog> blogsTarget = blogs.stream()
					.filter(blog -> blog.getTagIds().contains(tag.getId().toString()))
					.collect(Collectors.toList());
			tag.setBlogs(blogsTarget);
		});
		tags.sort((t1, t2) -> Integer.compare(t2.getBlogs().size(), t1.getBlogs().size()));
		//tags.stream().sorted((t1,t2) -> Integer.compare(t1.getBlogs().size(),t2.getBlogs().size()));
//		tags.stream().forEach(e -> {
//			List<Blog> blogs = blogService.query().like("tag_ids", e.getId()).list();
//			e.setBlogs(blogs);
//		});
		return tags;
	}

	@Override
	public List<Tag> getTags() {
		List<Tag> tags = list();
		return tags;
	}
}
