package com.springboot.betterreads.service;

import com.springboot.betterreads.domain.UserBooksVO;
import com.springboot.betterreads.exception.BadRequestException;
import com.springboot.betterreads.exception.BetterReadsException;

public interface UserBooksService {

	UserBooksVO getUserBooks(String userId, String bookId) throws BadRequestException;

	UserBooksVO saveUserBooks(String userId, String bookId, UserBooksVO userBooksVO)
			throws BadRequestException, BetterReadsException;

}
