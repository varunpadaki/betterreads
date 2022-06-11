package com.springboot.betterreads.domain;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import lombok.Getter;
import lombok.Setter;

@PrimaryKeyClass
@Getter
@Setter
public class UserBooksPrimaryKey {

	@PrimaryKeyColumn(name = "user_id",ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String userId;
	@PrimaryKeyColumn(name = "book_id",ordinal = 1, type = PrimaryKeyType.PARTITIONED)
	private String bookId;
	
	public UserBooksPrimaryKey() {
		
	}
	
	public UserBooksPrimaryKey(String userId,String bookId) {
		this.userId = userId;
		this.bookId = bookId;
	}
}
