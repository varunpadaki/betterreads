package com.springboot.betterreads.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.springboot.betterreads.domain.Book;
import com.springboot.betterreads.domain.BookVO;
import com.springboot.betterreads.domain.UserBooksVO;
import com.springboot.betterreads.exception.BadRequestException;
import com.springboot.betterreads.exception.RecordNotFoundException;
import com.springboot.betterreads.helper.BookHelper;
import com.springboot.betterreads.repository.BookRepository;

@Service
public class BookServiceImpl implements BookService {

	private Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

	@Autowired
	private BookHelper bookHelper;

	@Autowired
	private BookRepository bookRepository;

	@Override
	public BookVO getBookById(String bookId, String authHeader) throws RecordNotFoundException, BadRequestException {
		// TODO Auto-generated method stub
		if (!StringUtils.hasLength(bookId)) {
			String errorMessage = "Book ID is empty or null, cannot fetch book details.";
			logger.error(errorMessage);
			// set custom error code if defined else set httpstatus code
			throw new BadRequestException(errorMessage, errorMessage, HttpStatus.BAD_REQUEST.toString());
		}
		logger.info("Book ID : {}", bookId);
		Optional<Book> bookObj = bookRepository.findById(bookId);
		if (bookObj.isEmpty()) {
			String errorMessage = String.format("Book not found for ID : %s", bookId);
			logger.error(errorMessage);
			throw new RecordNotFoundException(errorMessage, errorMessage, HttpStatus.NOT_FOUND.toString());
		}
		logger.info("Book found for ID : {}", bookId);
		BookVO bookVO = new BookVO();
		BeanUtils.copyProperties(bookObj.get(), bookVO);
		bookHelper.setCoverImageUrl(bookVO);
		if (StringUtils.hasLength(authHeader)) {
			logger.info("User logged in< fetching book information for logged in user.");
			UserBooksVO userBooksVO = bookHelper.fetchUserBookInfo(authHeader, bookId);
			if(userBooksVO != null) {
				bookVO.setUserId(userBooksVO.getKey().getUserId());
				bookVO.setUserBooksVO(userBooksVO);
			}
		}
		return bookVO;
	}
}
