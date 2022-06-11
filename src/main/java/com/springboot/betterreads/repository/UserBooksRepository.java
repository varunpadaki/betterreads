package com.springboot.betterreads.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;

import com.springboot.betterreads.domain.UserBooks;
import com.springboot.betterreads.domain.UserBooksPrimaryKey;

public interface UserBooksRepository extends CassandraRepository<UserBooks, UserBooksPrimaryKey> {

}
