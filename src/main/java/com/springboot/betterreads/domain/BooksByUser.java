package com.springboot.betterreads.domain;

import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import com.datastax.oss.driver.api.core.uuid.Uuids;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(value = "books_by_user")
public class BooksByUser {

	// all books for a user will go to one node
	@PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String id;

	@PrimaryKeyColumn(name = "book_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
	@CassandraType(type = Name.TEXT)
	private String bookId;

	@PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
	@CassandraType(type = Name.TIMEUUID)
	private UUID timeUuid;

	@PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)
	private String readingStatus;

	@Column(value = "book_name")
	@CassandraType(type = Name.TEXT)
	private String bookName;

	@Column(value = "author_names")
	@CassandraType(type = Name.LIST, typeArguments = Name.TEXT)
	private List<String> authorNames;

	@Column(value = "cover_ids")
	@CassandraType(type = Name.LIST, typeArguments = Name.TEXT)
	private List<String> coverIds;

	@Column(value = "rating")
	@CassandraType(type = Name.INT)
	private int rating;

	@Transient
	private String coverUrl;
	
	 public BooksByUser() {
	        this.timeUuid = Uuids.timeBased();
	    }
}