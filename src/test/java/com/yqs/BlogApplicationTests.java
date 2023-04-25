package com.yqs;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mysql.cj.util.TimeUtil;
import com.yqs.dao.UserDao;
import com.yqs.pojo.Blog;
import com.yqs.pojo.Tag;
import com.yqs.pojo.User;
import com.yqs.service.IBlogService;
import com.yqs.service.ITagService;
import com.yqs.service.IUserService;
import com.yqs.service.impl.UserServiceImpl;
import com.yqs.util.MarkdownUtils;
import com.yqs.util.SystemConstans;
import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

//@MapperScan("com.yqs.mapper")
@SpringBootTest
class BlogApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private IUserService userService;

	@Autowired
	private UserDao userDao;

	@Autowired
	private IBlogService blogService;

	@Autowired
	private ITagService tagService;

	@Test
	void contextLoads() {
		userService.save(
				new User()
						.setId(1L)
						.setAvatar("test")
						.setCreateTime(DateUtil.now())
						.setEmail("2193215221@qq.com")
						.setNickname("清和")
						.setPassword("06095417Yqs")
						.setType(1)
						.setUpdateTime(DateUtil.now())
						.setUsername("尹清帅"));
	}

	@Test
	void test() {
		System.out.println(applicationContext.getBeansOfType(IUserService.class));
		//System.out.println(applicationContext.getBeansOfType(UserDao.class));
	}

	@Test
	void testMybatis() {
		User user = userDao.selectById(1L);
		System.out.println(user);
	}

	@Test
	void checkUser() {
		User user = userService.checkUser("2193215221@qq.com", "06095417Yqs");
		System.out.println(user);
	}

	@Test
	void testEq() {
		QueryWrapper<Blog> wrapper = new QueryWrapper<>();
		QueryWrapper<Blog> queryWrapper = wrapper.like("title", "test")
				.eq("type", "test")
				.eq("recommended", true);
		Blog one = blogService.getOne(queryWrapper);
		System.out.println(one);
	}

	@Test
	void testStream() {

		ArrayList<Tag> tags = new ArrayList<>();
		Tag t1 = new Tag();
		t1.setId(1L);
		t1.setName("test1");
		Tag t2 = new Tag();
		t1.setId(2L);
		t1.setName("test2");
		Tag t3 = new Tag();
		t1.setId(3L);
		t1.setName("test3");
		Tag t4 = new Tag();
		t1.setId(4L);
		t1.setName("test4");
		tags.add(t1);
		tags.add(t2);
		tags.add(t3);
		tags.add(t4);
		System.out.println();
	}

	@Test
	void update() {
		Blog b = new Blog();
		b.setId(93L);
		boolean isSuccess = blogService.update(b, new UpdateWrapper<Blog>().eq("id",b.getId()).set("views", blogService.getById(b.getId()).getViews() + 1));
	}

	@Test
	void testUtils() {
		String s = "5,4";
		Arrays.stream(s.split(",")).forEach(e -> {
			Tag tag = tagService.getById(e);
			System.out.println(tag);
		});
		Blog blog = blogService.getById(93L);
		Date createTime = blog.getCreateTime();
	}
}
