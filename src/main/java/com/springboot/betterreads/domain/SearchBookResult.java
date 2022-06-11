package com.springboot.betterreads.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchBookResult {
	private String key;
	private String title;
	private List<String> author_name;
	private String cover_i;
	private int first_publish_year;
}
