package com.yqs.dto;

import lombok.Data;
import org.springframework.stereotype.Repository;

@SuppressWarnings({"all"})
/**
 * 记录请求信息
 */
@Repository
@Data
public class RequestLog {
	private String url;
	private String ip;
	private String classMethod;
	private Object[] args;
}
