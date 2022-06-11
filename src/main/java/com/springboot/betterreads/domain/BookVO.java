package com.springboot.betterreads.domain;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookVO {

	private String id;
	private String name;
	private String description;
	private LocalDate publishedDate;
	private List<String> coverIds;
	private List<String> authorNames;
	private List<String> authorId;
	private String coverImageUrl;
	private String userId;
	private UserBooksVO userBooksVO;
}