package com.springboot.betterreads.controller;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.betterreads.domain.ErrorResponse;
import com.springboot.betterreads.domain.UserBooksVO;
import com.springboot.betterreads.exception.BadRequestException;
import com.springboot.betterreads.exception.BetterReadsException;
import com.springboot.betterreads.service.UserBooksService;

@RestController
@RequestMapping("/better-reads")
public class UserBooksController {

	private Logger logger = LoggerFactory.getLogger(UserBooksController.class);

	@Autowired
	private UserBooksService userBooksServiceImpl;

	@GetMapping(value = "/users/{userId}/books/{bookId}")
	public ResponseEntity<Object> getUserBooks(@PathVariable(name = "userId", required = true) String userId,
			@PathVariable(name = "bookId", required = true) String bookId) {
		try {
			UserBooksVO userBooksVO = userBooksServiceImpl.getUserBooks(userId, bookId);
			if (userBooksVO != null) {
				return new ResponseEntity<>(userBooksVO, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (BadRequestException e) {
			ErrorResponse errorResponse = new ErrorResponse();
			// set custom error code if defined else set httpstatus code
			errorResponse.setErrorCode(e.getErrorCode());
			errorResponse.setErrorMessage(e.getErrorMessage());
			errorResponse.setDebugMessage(e.getDebugMessage());
			errorResponse.setTimestamp(Instant.now().toEpochMilli());
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			String errorMessage = String.format(
					"Error occurred in UserBooksController getUserBooks for userId : %s and bookId : %s", userId,
					bookId);
			logger.error(errorMessage, e);
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
			errorResponse.setErrorMessage(e.getMessage());
			errorResponse.setDebugMessage(e.getMessage());
			errorResponse.setTimestamp(Instant.now().toEpochMilli());
			return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(value = "/users/{userId}/books/{bookId}")
	public ResponseEntity<Object> saveUserBooks(@PathVariable(name = "userId", required = true) String userId,
			@PathVariable(name = "bookId", required = true) String bookId, @RequestBody UserBooksVO userBooksVO) {
		try {
			UserBooksVO savedUserBooksVO = userBooksServiceImpl.saveUserBooks(userId, bookId, userBooksVO);
			return new ResponseEntity<>(savedUserBooksVO, HttpStatus.OK);
		} catch (BadRequestException e) {
			ErrorResponse errorResponse = new ErrorResponse();
			// set custom error code if defined else set httpstatus code
			errorResponse.setErrorCode(e.getErrorCode());
			errorResponse.setErrorMessage(e.getErrorMessage());
			errorResponse.setDebugMessage(e.getDebugMessage());
			errorResponse.setTimestamp(Instant.now().toEpochMilli());
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		} catch (BetterReadsException e) {
			ErrorResponse errorResponse = new ErrorResponse();
			// set custom error code if defined else set httpstatus code
			errorResponse.setErrorCode(e.getErrorCode());
			errorResponse.setErrorMessage(e.getMessage());
			errorResponse.setDebugMessage(e.getDebugMessage());
			errorResponse.setTimestamp(Instant.now().toEpochMilli());
			return new ResponseEntity<>(errorResponse, e.getHttpStatus());
		} catch (Exception e) {
			// any other exception is considered as Internal Server Error
			String errorMessage = String.format(
					"Error occurred in UserBooksController getUserBooks for userId : %s and bookId : %s", userId,
					bookId);
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