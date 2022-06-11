package com.springboot.betterreads.controller;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.betterreads.domain.BookVO;
import com.springboot.betterreads.domain.ErrorResponse;
import com.springboot.betterreads.exception.BadRequestException;
import com.springboot.betterreads.exception.RecordNotFoundException;
import com.springboot.betterreads.service.BookService;

@RestController
@RequestMapping(value = "/better-reads")
public class BookController {

	private Logger logger = LoggerFactory.getLogger(BookController.class);

	@Autowired
	private BookService bookServiceImpl;

	@GetMapping(value = "/books/{bookId}")
	public ResponseEntity<Object> getBookById(@PathVariable(name = "bookId") String bookId,@RequestHeader(name = "Authorization",required = false) String authHeader) {
		try {
			BookVO bookVO = bookServiceImpl.getBookById(bookId,authHeader);
			return new ResponseEntity<>(bookVO, HttpStatus.OK);
		} catch (RecordNotFoundException e) {
			ErrorResponse errorResponse = new ErrorResponse();
			//set custom error code if defined else set httpstatus code
			errorResponse.setErrorCode(e.getErrorCode());
			errorResponse.setErrorMessage(e.getErrorMessage());
			errorResponse.setDebugMessage(e.getDebugMessage());
			errorResponse.setTimestamp(Instant.now().toEpochMilli());
			return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
		} catch(BadRequestException e) {
			ErrorResponse errorResponse = new ErrorResponse();
			//set custom error code if defined else set httpstatus code
			errorResponse.setErrorCode(e.getErrorCode());
			errorResponse.setErrorMessage(e.getErrorMessage());
			errorResponse.setDebugMessage(e.getDebugMessage());
			errorResponse.setTimestamp(Instant.now().toEpochMilli());
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}catch (Exception e) {
			String errorMessage = String.format("Error occurred in BookController grtBookById for %s", bookId);
			logger.error(errorMessage, e);
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
			errorResponse.setErrorMessage(e.getMessage());
			errorResponse.setDebugMessage(e.getMessage());
			errorResponse.setTimestamp(Instant.now().toEpochMilli());
			return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
