package com.springboot.betterreads.controller;

import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.betterreads.domain.BooksByUser;
import com.springboot.betterreads.domain.ErrorResponse;
import com.springboot.betterreads.exception.BadRequestException;
import com.springboot.betterreads.exception.RecordNotFoundException;
import com.springboot.betterreads.service.DashboardService;

@RestController
@RequestMapping("/better-reads")
public class DashboardController {

	private Logger logger = LoggerFactory.getLogger(DashboardController.class);

	@Autowired
	private DashboardService dashboardServiceImpl;

	@GetMapping("/dashboard/{userId}")
	public ResponseEntity<Object> getBooksByUser(@PathVariable(name = "userId", required = true) String userId) {
		try {
			List<BooksByUser> booksByUser = dashboardServiceImpl.getBooksByUser(userId);
			return new ResponseEntity<>(booksByUser, HttpStatus.OK);
		} catch (BadRequestException e) {
			ErrorResponse errorResponse = new ErrorResponse();
			// set custom error code if defined else set httpstatus code
			errorResponse.setErrorCode(e.getErrorCode());
			errorResponse.setErrorMessage(e.getErrorMessage());
			errorResponse.setDebugMessage(e.getDebugMessage());
			errorResponse.setTimestamp(Instant.now().toEpochMilli());
			return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
		} catch (RecordNotFoundException e) {
			ErrorResponse errorResponse = new ErrorResponse();
			// set custom error code if defined else set httpstatus code
			errorResponse.setErrorCode(e.getErrorCode());
			errorResponse.setErrorMessage(e.getErrorMessage());
			errorResponse.setDebugMessage(e.getDebugMessage());
			errorResponse.setTimestamp(Instant.now().toEpochMilli());
			return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			String errorMessage = String.format("Error occurred in DashboardController getBooksByUser for %s", userId);
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
