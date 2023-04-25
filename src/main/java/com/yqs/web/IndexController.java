package com.yqs.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yqs.pojo.Blog;
import com.yqs.pojo.Tag;
import com.yqs.pojo.Type;
import com.yqs.pojo.User;
import com.yqs.service.IBlogService;
import com.yqs.service.ITagService;
import com.yqs.service.ITypeService;
import com.yqs.service.IUserService;
import com.yqs.util.SystemConstans;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings({"all"})
@Controller
public class IndexController {

	@Resource
	private IBlogService blogService;

	@Resource
	private ITypeService typeService;

	@Resource
	private ITagService tagService;

	@Resource
	private IUserService userService;

	@GetMapping("/")
	public String index(@RequestParam(value = "current", defaultValue = "1")Integer current,
	                    Model model) {
		Page<Blog> blogPage = blogService.pageBlog(current, SystemConstans.DEFAULT_PAGE_SIZE, false);
		List<Blog> blogs = blogService.list();
		List<Tag> tags = tagService.initTags(blogs);
		List<Type> types = typeService.initTypes(blogs);
		List<Blog> recommend = blogService.query().eq("recommend", true).list().stream().collect(Collectors.toList());
		model.addAttribute("page", blogPage);
		model.addAttribute("tags", tags);
		model.addAttribute("types", types);
		model.addAttribute("recommendBlogs", recommend);
		model.addAttribute("first", current == 1 ? true : false);
		model.addAttribute("last", current * blogPage.getSize() >= blogPage.getTotal() ? true : false);
		return "index";
	}
	@GetMapping("/blog/{id}")
	public String blog(@PathVariable Long id, Model model) {
		User user = userService.getById(blogService.getById(id).getUserId());
		model.addAttribute("tags",tagService.list());
		model.addAttribute("blog", blogService.getAndConvert(id,user));
		//model.addAttribute("blog", blogService.getBlog(id,user));
		return "blog";
	}

	@PostMapping("/search")
	public String search(@RequestParam(value = "current", defaultValue = "1")Integer current,
	                     @RequestParam String query,
	                     Model model) {
		Page<Blog> searchBlogs = blogService.search(current, SystemConstans.DEFAULT_PAGE_SIZE, query);
		model.addAttribute("page",searchBlogs);
		model.addAttribute("first", current == 1 ? true : false);
		model.addAttribute("last", current * searchBlogs.getSize() >= searchBlogs.getTotal() ? true : false);
		model.addAttribute("query", query);
		return "search";
	}

	@GetMapping("/footer/newblog")
	public String newblogs(Model model) {
		List<Blog> blogs = blogService.list();
		blogs.sort((b1, b2) -> b1.getCreateTime().compareTo(b2.getCreateTime()));
		List<Blog> newblogs = blogs.stream().limit(SystemConstans.NEW_BLOGS_SIZE).collect(Collectors.toList());
		model.addAttribute("newblogs", newblogs);
		return "_fragments::newblogList";
	}

}
