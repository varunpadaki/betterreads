package com.springboot.betterreads.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;

import com.springboot.betterreads.domain.Book;

public interface BookRepository extends CassandraRepository<Book, String>{

}
