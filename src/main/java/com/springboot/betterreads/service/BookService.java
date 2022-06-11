package com.springboot.betterreads.service;

import com.springboot.betterreads.domain.BookVO;
import com.springboot.betterreads.exception.BadRequestException;
import com.springboot.betterreads.exception.RecordNotFoundException;

public interface BookService {

	public BookVO getBookById(String bookId, String authHeader) throws RecordNotFoundException, BadRequestException;
}
