package com.springboot.betterreads.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
	private String errorMessage;
	private String debugMessage;
	//custom error code specific to application
	private String errorCode;
	private long timestamp;
	private String path;
}
