package com.yqs.web;

import com.yqs.pojo.Comment;
import com.yqs.pojo.User;
import com.yqs.service.IBlogService;
import com.yqs.service.ICommentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@SuppressWarnings({"all"})
@Controller
public class CommentController {

	@Resource
	private ICommentService commentService;

	@Resource
	private IBlogService blogService;

	@Value("${comment.avatar}")
	private String avatar;

	@GetMapping("/comments/{id}")
	public String comments(@PathVariable Long id, Model model) {
		model.addAttribute("comments",commentService.getByBlogId(id));
		return "blog::commentList";
	}

	@PostMapping("/comments")
	public String post(Comment comment, HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user != null) {
			comment.setAdminComment(true);
			comment.setAvatar(user.getAvatar());
			comment.setNickname(user.getNickname());
			comment.setEmail(user.getEmail());
		} else {
			comment.setAdminComment(false);
			comment.setAvatar(avatar);
		}
		commentService.saveComment(comment);
		return "redirect:/comments/" + comment.getBlogId();
	}
}
