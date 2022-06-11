package com.springboot.betterreads.utils;

public interface BetterReadsConstants {

	public static final String COVER_IMAGE_URL = "http://covers.openlibrary.org/b/id/";
	public static final String ACTUATOR_URI = "/actuator";
	public static final String AUTHORIZATION = "authorization";
	public static final String BEARER = "Bearer";
	public static final String SPACE_DELIMITER = " ";
	public static final String JWT_TOKEN_VALIDATION_FAILURE_MSG = "Invalid JWT token, cannot access API.";
	public static final String CONTENT_TYPE_JSON = "application/json";
	public static final String GET_BOOK_BY_ID_URI = "/books";
}
