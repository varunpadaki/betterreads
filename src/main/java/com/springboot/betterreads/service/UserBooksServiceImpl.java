package com.springboot.betterreads.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.springboot.betterreads.domain.UserBooks;
import com.springboot.betterreads.domain.UserBooksPrimaryKey;
import com.springboot.betterreads.domain.UserBooksVO;
import com.springboot.betterreads.exception.BadRequestException;
import com.springboot.betterreads.exception.BetterReadsException;
import com.springboot.betterreads.helper.BookHelper;
import com.springboot.betterreads.repository.UserBooksRepository;

@Service
public class UserBooksServiceImpl implements UserBooksService {

	@Autowired
	private UserBooksRepository userBooksRepository;
	
	@Autowired
	private BookHelper bookHelper;

	private Logger logger = LoggerFactory.getLogger(UserBooksServiceImpl.class);

	@Override
	public UserBooksVO getUserBooks(String userId, String bookId) throws BadRequestException {

		if (!StringUtils.hasLength(bookId)) {
			String errorMessage = "Book ID is empty, cannot fetch user books.";
			logger.error(errorMessage);
			//Use custom error code if present else use the HTTP STATUS only
			throw new BadRequestException(errorMessage,errorMessage,HttpStatus.BAD_REQUEST.toString());
		}
		if (!StringUtils.hasLength(userId)) {
			String errorMessage = "User ID is empty, cannot fetch user books.";
			logger.error(errorMessage);
			//Use custom error code if present else use the HTTP STATUS only
			throw new BadRequestException(errorMessage,errorMessage,HttpStatus.BAD_REQUEST.toString());
		}

		Optional<UserBooks> userBooksObj = userBooksRepository.findById(new UserBooksPrimaryKey(userId, bookId));
		if (userBooksObj.isPresent()) {
			logger.info("Book information found successfully for userID : {}, bookID : {}", userId, bookId);
			UserBooksVO userBooksVO = new UserBooksVO();
			BeanUtils.copyProperties(userBooksObj.get(), userBooksVO);
			return userBooksVO;
		}
		logger.error("Book information not found for user ID : {} and book ID : {}", userId, bookId);
		return null;
	}

	@Override
	public UserBooksVO saveUserBooks(String userId, String bookId, UserBooksVO userBooksVO)
			throws BadRequestException, BetterReadsException {
		if (!StringUtils.hasLength(bookId)) {
			String errorMessage = "Book ID is empty, cannot save user books.";
			logger.error(errorMessage);
			//Use custom error code if present else use the HTTP STATUS only
			throw new BadRequestException(errorMessage,errorMessage,HttpStatus.BAD_REQUEST.toString());
		}
		if (!StringUtils.hasLength(userId)) {
			String errorMessage = "User ID is empty, cannot save user books.";
			logger.error(errorMessage);
			throw new BadRequestException(errorMessage,errorMessage,HttpStatus.BAD_REQUEST.toString());
		}

		if (userBooksVO == null) {
			String errorMessage = "UserBooks object is empty, cannot save user books.";
			logger.error(errorMessage);
			throw new BadRequestException(errorMessage,errorMessage,HttpStatus.BAD_REQUEST.toString());
		}
		
		UserBooks userBooks = new UserBooks();
		BeanUtils.copyProperties(userBooksVO, userBooks);
		UserBooksPrimaryKey key = new UserBooksPrimaryKey(userId,bookId);
		userBooks.setKey(key);
		UserBooks savedUserBook = userBooksRepository.save(userBooks);
		if (savedUserBook != null) {
			//also save book by user this is for dashboard to display books information for a logged in user
			userBooksVO.setKey(key);
			bookHelper.addBookByUser(userBooksVO);
			return userBooksVO;
		}
		String errorMessage = String.format("Failed to save user books for userId : %s and bookId : %s", userId,
				bookId);
		logger.error(errorMessage);
		throw new BetterReadsException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR, errorMessage,
				HttpStatus.INTERNAL_SERVER_ERROR.toString());
	}
}
