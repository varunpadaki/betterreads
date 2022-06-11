package com.springboot.betterreads.service;

import java.util.List;

import com.springboot.betterreads.domain.BooksByUser;
import com.springboot.betterreads.exception.BadRequestException;
import com.springboot.betterreads.exception.RecordNotFoundException;

public interface DashboardService {

	List<BooksByUser> getBooksByUser(String userId) throws BadRequestException, RecordNotFoundException;

	BooksByUser saveBooksByUser(BooksByUser booksByUser) throws BadRequestException;

}
