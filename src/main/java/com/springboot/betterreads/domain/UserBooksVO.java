package com.springboot.betterreads.domain;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBooksVO {
	private UserBooksPrimaryKey key;
	private LocalDate startDate;
	private LocalDate completedDate;
	private String readingStatus;
	private int rating;
	
	
	//add additional book information
	private String name;
	private List<String> coverIds;
	private List<String> authorNames;
}
