package com.springboot.betterreads.controller;

import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.betterreads.domain.ErrorResponse;
import com.springboot.betterreads.domain.SearchBookResult;
import com.springboot.betterreads.exception.BadRequestException;
import com.springboot.betterreads.exception.RecordNotFoundException;
import com.springboot.betterreads.service.SearchBookService;

@RestController
@RequestMapping(value = "/better-reads/search")
public class SearchBookController {

	private Logger logger = LoggerFactory.getLogger(SearchBookController.class);
	
	@Autowired
	private SearchBookService searchBookService;
	
	@GetMapping(value = "/books")
	public ResponseEntity<Object> searchBooks(@RequestParam String query) {
		try {
			List<SearchBookResult> books = searchBookService.searchBooks(query);
			return new ResponseEntity<>(books,HttpStatus.OK);
		} catch (RecordNotFoundException e) {
			ErrorResponse errorResponse = new ErrorResponse();
			//set custom error code if defined else set httpstatus code
			errorResponse.setErrorCode(e.getErrorCode());
			errorResponse.setErrorMessage(e.getErrorMessage());
			errorResponse.setDebugMessage(e.getDebugMessage());
			errorResponse.setTimestamp(Instant.now().toEpochMilli());
			return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
		} catch (BadRequestException e) {
			ErrorResponse errorResponse = new ErrorResponse();
			//set custom error code if defined else set httpstatus code
			errorResponse.setErrorCode(e.getErrorCode());
			errorResponse.setErrorMessage(e.getErrorMessage());
			errorResponse.setDebugMessage(e.getDebugMessage());
			errorResponse.setTimestamp(Instant.now().toEpochMilli());
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		} catch(Exception e) {
			String errorMessage = String.format("Error occurred in SearchBookController searchBooks for queryParam %s", query);
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
