package com.yqs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings({"all"})
@ResponseStatus(HttpStatus.NOT_FOUND)
public class BlogNotFoundException extends RuntimeException{
	public BlogNotFoundException() {
	}

	public BlogNotFoundException(String message) {
		super(message);
	}

	public BlogNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
