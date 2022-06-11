package com.springboot.betterreads.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.springboot.betterreads.domain.BooksByUser;

public interface BooksByUserRepository extends CassandraRepository<BooksByUser, String> {
	//overloaded method so no need of @Query annotation, spring data will manage automatically
	Slice<BooksByUser> findAllById(String userId, Pageable pageable);
}
