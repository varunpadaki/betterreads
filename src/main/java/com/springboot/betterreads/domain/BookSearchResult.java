package com.springboot.betterreads.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookSearchResult {
	private int numFound;
	private List<SearchBookResult> docs;
}
