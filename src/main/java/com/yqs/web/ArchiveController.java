package com.yqs.web;

import com.yqs.pojo.Blog;
import com.yqs.service.IBlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"all"})
@Controller
public class ArchiveController {

	@Resource
	private IBlogService blogService;

	@GetMapping("/archives")
	public String archives(Model model) {
		List<Blog> blogs = blogService.list();
		Map<Integer, List<Blog>> archiveMap = blogs
				.stream()
				.collect(Collectors.groupingBy(blog -> {
					LocalDateTime dateTime = LocalDateTime.ofInstant(blog.getCreateTime().toInstant(), ZoneId.systemDefault());
					return dateTime.getYear();
				}));
		int blogCount = blogs.size();
		model.addAttribute("archiveMap", archiveMap);
		model.addAttribute("blogCount", blogCount);
		return "archives";
	}
}
