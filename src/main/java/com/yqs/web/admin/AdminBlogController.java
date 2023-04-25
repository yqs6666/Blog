package com.yqs.web.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yqs.pojo.Blog;
import com.yqs.pojo.User;
import com.yqs.service.IBlogService;
import com.yqs.service.ITagService;
import com.yqs.service.ITypeService;
import com.yqs.util.SystemConstans;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@SuppressWarnings({"all"})
@Controller
@RequestMapping("/admin")
public class AdminBlogController {

	@Resource
	private IBlogService blogService;

	@Resource
	private ITypeService typeService;

	@Resource
	private ITagService tagService;

	@GetMapping("/blogs")
	public String blogs(@RequestParam(value = "current", defaultValue = "1")Integer current,
	                    Model model) {
		Page<Blog> page = blogService.pageBlog(current, SystemConstans.DEFAULT_PAGE_SIZE,true);
		model.addAttribute("types",typeService.list());
		model.addAttribute("page", page);
		model.addAttribute("first", current == 1 ? true : false);
		model.addAttribute("last", current * page.getSize() >= page.getTotal() ? true : false);
		return "admin/blogs";
	}

	@PostMapping("/blogs/search")
	public String blogsSearch(@RequestParam(value = "current", defaultValue = "1")Integer current,
	                    Model model,
	                          Blog blog,
	                          Long typeId) {
		Page<Blog> page = blogService.page(
				new Page(current, SystemConstans.DEFAULT_PAGE_SIZE));
		Page<Blog> blogPage = blogService.listBlogBySearch(page, blog, typeId);
		blogPage.getRecords().forEach(e -> e.setType(typeService.getById(e.getTypeId())));
		model.addAttribute("page", blogPage);
		model.addAttribute("first", current == 1 ? true : false);
		model.addAttribute("last", current * page.getSize() >= page.getTotal() ? true : false);
		return "admin/blogs::blogList";
	}

	@GetMapping("/blogs/input")
	public String input(Model model) {
		model.addAttribute("blog",new Blog());
		setTypeAndTag(model);
		return "admin/blogs-input";
	}

	private void setTypeAndTag(Model model) {
		model.addAttribute("types",typeService.list());
		model.addAttribute("tags",tagService.list());
	}

	@PostMapping("/blogs")
	public String post(Blog blog, RedirectAttributes attributes, HttpSession session) {
		blog.setUser((User) session.getAttribute("user"));
		boolean isSuccess;
		if (blog.getId() != null) {
			isSuccess = blogService.updateBlog(blog);
		} else {
			isSuccess = blogService.saveBlog(blog);
		}

		if (!isSuccess) {
			attributes.addFlashAttribute("message","操作失败！");
		}
		attributes.addFlashAttribute("message","操作成功！");
		return "redirect:/admin/blogs";
	}

	@GetMapping("/blogs/{id}/input")
	public String editInput(@PathVariable Long id, Model model) {
		setTypeAndTag(model);
		model.addAttribute("blog",blogService.getById(id));
		return "admin/blogs-input";
	}

	@GetMapping("/blogs/{id}/delete")
	public String deleteBlog(@PathVariable Long id, RedirectAttributes attributes) {
		blogService.deleteBlog(id);
		attributes.addFlashAttribute("message","删除成功");
		return "redirect:/admin/blogs";
	}
}
