package com.springboot.betterreads.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthClaims {

	private String subject;
	private List<String> authorities;
}
