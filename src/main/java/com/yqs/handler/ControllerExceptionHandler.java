package com.yqs.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@SuppressWarnings({"all"})
/**
 * 异常处理
 */
@ControllerAdvice
public class ControllerExceptionHandler {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@ExceptionHandler(Exception.class)
	public ModelAndView exceptionHandler(HttpServletRequest request, Exception e) throws Exception {
		logger.error("Request URL : {}", request.getRequestURL(),e);

		//不处理有状态码的异常(交给SpringBoot本身)
		if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
			throw e;
		}

		ModelAndView mv = new ModelAndView();
		mv.addObject("url",request.getRequestURL());
		mv.addObject("exception",e);
		mv.setViewName("error/error");
		return mv;
	}
}
