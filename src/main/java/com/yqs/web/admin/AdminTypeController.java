package com.yqs.web.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yqs.pojo.Type;
import com.yqs.service.ITypeService;
import com.yqs.util.SystemConstans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@SuppressWarnings({"all"})
@Controller
@RequestMapping("/admin")
public class AdminTypeController {

	@Autowired
	private ITypeService typeService;

	@GetMapping("/types")
	public String types(@RequestParam(value = "current", defaultValue = "1") Integer current,
	                    Model model) {
		Page<Type> page = typeService.listType(new Page<>(current, SystemConstans.DEFAULT_PAGE_SIZE));
		model.addAttribute("page", page);
		model.addAttribute("first", current == 1 ? true : false);
		model.addAttribute("last", current * page.getSize() >= page.getTotal() ? true : false);
		return "admin/types";
	}

	@GetMapping("/types/input")
	public String input(Model model) {
		model.addAttribute("type", new Type());
		return "admin/types-input";
	}

	@PostMapping("/types")
	public String post(@Valid Type type, BindingResult result, RedirectAttributes attributes) {
		if (typeService.getOne(new QueryWrapper<Type>(type)) != null) {
			result.rejectValue("name","nameError","该分类已存在！");
		}
		if (result.hasErrors()) {
			return "admin/types-input";
		}
		boolean isSuccess = typeService.saveType(type);
		if (!isSuccess) {
			attributes.addFlashAttribute("message","新增失败！");
		}
		attributes.addFlashAttribute("message","新增成功！");
		return "redirect:/admin/types";
	}

	@GetMapping("/types/{id}/input")
	public String editInput(@PathVariable Long id, Model model) {
		model.addAttribute("type",typeService.getType(id));
		return "admin/types-input";
	}

	@PostMapping("/types/{id}")
	public String editPost(@Valid Type type, BindingResult result, @PathVariable Long id, RedirectAttributes attributes) {
		if (typeService.getOne(new QueryWrapper<Type>(type)) != null) {
			result.rejectValue("name","nameError","该分类已存在！");
		}
		if (result.hasErrors()) {
			return "admin/types-input";
		}
		boolean isSuccess = typeService.updateType(id, type);
		if (!isSuccess) {
			attributes.addFlashAttribute("message","更新失败！");
		}
		attributes.addFlashAttribute("message","更新成功！");
		return "redirect:/admin/types";
	}

	@GetMapping("/types/{id}/delete")
	public String delete(@PathVariable Long id, RedirectAttributes attributes) {
		boolean isSuccess = typeService.deleteType(id);
		if (!isSuccess) {
			attributes.addFlashAttribute("message","删除失败！");
		}
		attributes.addFlashAttribute("message","删除成功！");
		return "redirect:/admin/types";
	}
}
