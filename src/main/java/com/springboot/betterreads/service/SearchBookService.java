package com.springboot.betterreads.service;

import java.util.List;

import com.springboot.betterreads.domain.SearchBookResult;
import com.springboot.betterreads.exception.BadRequestException;
import com.springboot.betterreads.exception.RecordNotFoundException;

public interface SearchBookService {
	public List<SearchBookResult> searchBooks(String query) throws RecordNotFoundException, BadRequestException;
}
