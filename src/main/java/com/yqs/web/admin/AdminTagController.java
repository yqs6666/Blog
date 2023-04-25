package com.yqs.web.admin;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yqs.exception.BlogNotFoundException;
import com.yqs.pojo.Tag;
import com.yqs.service.ITagService;
import com.yqs.util.SystemConstans;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.Valid;

@SuppressWarnings({"all"})
@Controller
@RequestMapping("/admin")
public class AdminTagController {

	@Resource
	private ITagService tagService;

	@GetMapping("/tags")
	public String tags(@RequestParam(value = "current", defaultValue = "1")Integer current,
	                   Model model) {
		Page<Tag> page = tagService.page(new Page<>(current, SystemConstans.DEFAULT_PAGE_SIZE));
		model.addAttribute("page", page);
		model.addAttribute("first", current == 1 ? true : false);
		model.addAttribute("last", current * page.getSize() >= page.getTotal() ? true : false);
		return "admin/tags";
	}

	@GetMapping("/tags/input")
	public String input(Model model) {
		model.addAttribute("tag", new Tag());
		return "admin/tags-input";
	}

	@PostMapping("/tags")
	public String post(@Valid Tag tag, BindingResult result, RedirectAttributes attributes) {
		if (tagService.getOne(new QueryWrapper<Tag>(tag)) != null) {
			result.rejectValue("name","nameError","该标签已存在！");
		}
		if (result.hasErrors()) {
			return "admin/tags-input";
		}
		boolean isSuccess = tagService.save(tag);
		if (!isSuccess) {
			attributes.addFlashAttribute("message","新增失败！");
		}
		attributes.addFlashAttribute("message","新增成功！");
		return "redirect:/admin/tags";
	}

	@GetMapping("/tags/{id}/input")
	public String editInput(@PathVariable Long id, Model model) {
		model.addAttribute("tag",tagService.getById(id));
		return "admin/tags-input";
	}

	@PostMapping("/tags/{id}")
	public String editPost(@Valid Tag tag, BindingResult result, @PathVariable Long id, RedirectAttributes attributes) {
		if (tagService.getOne(new QueryWrapper<Tag>(tag)) != null) {
			result.rejectValue("name","nameError","该标签已存在！");
		}
		if (result.hasErrors()) {
			return "admin/types-input";
		}
		Tag t = tagService.getById(id);
		if (t == null) {
			throw new BlogNotFoundException();
		}
		BeanUtil.copyProperties(tag, t);
		boolean isSuccess = tagService.updateById(t);
		if (!isSuccess) {
			attributes.addFlashAttribute("message","更新失败！");
		}
		attributes.addFlashAttribute("message","更新成功！");
		return "redirect:/admin/tags";
	}

	@GetMapping("/tags/{id}/delete")
	public String delete(@PathVariable Long id, RedirectAttributes attributes) {
		boolean isSuccess = tagService.removeById(id);
		if (!isSuccess) {
			attributes.addFlashAttribute("message","删除失败！");
		}
		attributes.addFlashAttribute("message","删除成功！");
		return "redirect:/admin/tags";
	}
}
