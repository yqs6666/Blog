package com.yqs.aspect;

import com.yqs.dto.RequestLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@SuppressWarnings({"all"})
/**
 * 记录日志
 * @doBefore 请求url、访问者ip、调用方法classMethod、参数args
 * @doAfterReturn 返回内容
 */
@Aspect
@Component
public class LogAspect {

	@Autowired
	private RequestLog requestLog;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	//切面 web下的所有类的所有方法
	@Pointcut("execution(* com.yqs.web.*.*(..))")
	public void log() {}

	@Before("log()")
	public void doBefore(JoinPoint joinPoint) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		String url = request.getRequestURL().toString();
		String ip = request.getRemoteAddr();
		String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
		Object[] args = joinPoint.getArgs();
		requestLog.setIp(ip);
		requestLog.setUrl(url);
		requestLog.setClassMethod(classMethod);
		requestLog.setArgs(args);
		logger.info("Request : {}", requestLog);
	}

	@After("log()")
	public void doAfter() {
	//	logger.info("----------doAfter----------");
	}

	@AfterReturning(returning = "result", pointcut = "log()")
	public void doAfterReturn(Object result) {
		logger.info("Result : {}", result);
	}
}
