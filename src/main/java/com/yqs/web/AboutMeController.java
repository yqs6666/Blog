package com.yqs.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SuppressWarnings({"all"})
@Controller
public class AboutMeController {

	@GetMapping("/about")
	public String about() {
		return "about";
	}
}
