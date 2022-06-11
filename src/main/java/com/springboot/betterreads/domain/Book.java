package com.springboot.betterreads.domain;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.Getter;
import lombok.Setter;

@Table(value = "book_by_id")
@Getter
@Setter
public class Book {

	@Id
	@PrimaryKeyColumn(name="book_id",ordinal=0,type=PrimaryKeyType.PARTITIONED)
	private String id;
	
	@Column("book_name")
	@CassandraType(type = Name.TEXT)
	private String name;
	
	@Column("book_description")
	@CassandraType(type = Name.TEXT)
	private String description;
	
	@Column("published_date")
	@CassandraType(type = Name.DATE)
	private LocalDate publishedDate;
	
	@Column("cover_ids")
	@CassandraType(type = Name.LIST,typeArguments = Name.TEXT) //List<String>
	private List<String> coverIds;
	
	@Column("author_names")
	@CassandraType(type = Name.LIST,typeArguments = Name.TEXT) //List<String>
	private List<String> authorNames;
	
	@Column("author_id")
	@CassandraType(type = Name.LIST,typeArguments = Name.TEXT) //List<String>
	private List<String> authorId;
}