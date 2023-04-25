package com.yqs.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yqs.pojo.Tag;
import com.yqs.pojo.Type;
import com.yqs.service.IBlogService;
import com.yqs.service.ITagService;
import com.yqs.util.SystemConstans;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

@SuppressWarnings({"all"})
@Controller
public class TagController {

	@Resource
	public ITagService tagService;

	@Resource
	private IBlogService blogService;

	@GetMapping("/tags/{id}")
	public String types(@PathVariable Long id,
	                    @RequestParam(value = "current", defaultValue = "1") Integer current,
	                    Model model) {
		List<Tag> tags = tagService.getTags();
		tags.stream().forEach(tag -> {
			tag.setBlogs(blogService.query().like("tag_ids", tag.getId()).list());
		});
		tags.sort((t1, t2) -> Integer.compare(t2.getBlogs().size(),t1.getBlogs().size()));
		if (id == -1) {
			id = tags.get(0).getId();
		}
		Page page = blogService.pageBlogByTags(current, SystemConstans.DEFAULT_PAGE_SIZE,id);
		model.addAttribute("tags", tags);
		model.addAttribute("page", page);
		model.addAttribute("first", current == 1 ? true : false);
		model.addAttribute("last", current * page.getSize() >= page.getTotal() ? true : false);
		model.addAttribute("activeTagId",id);
		return "tags";
	}
}
