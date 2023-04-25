package com.yqs.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yqs.pojo.Blog;
import com.yqs.pojo.Type;
import com.yqs.service.IBlogService;
import com.yqs.service.ITypeService;
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
public class TypeController {

	@Resource
	private ITypeService typeService;

	@Resource
	private IBlogService blogService;

	@GetMapping("/types/{id}")
	public String types(@PathVariable Long id,
	                    @RequestParam(value = "current", defaultValue = "1") Integer current,
	                    Model model) {
		List<Type> types = typeService.getTypes();
		types.stream().forEach(type -> {
			type.setBlogs(blogService.query().eq("type_id", type.getId()).list());
		});
		types.sort((t1, t2) -> Integer.compare(t2.getBlogs().size(),t1.getBlogs().size()));
		if (id == -1) {
			id = types.get(0).getId();
		}
 		Page page = blogService.pageBlogByType(current, SystemConstans.DEFAULT_PAGE_SIZE,id);
		model.addAttribute("types", types);
		model.addAttribute("page", page);
		model.addAttribute("first", current == 1 ? true : false);
		model.addAttribute("last", current * page.getSize() >= page.getTotal() ? true : false);
		model.addAttribute("activeTypeId",id);
		return "types";
	}
}
