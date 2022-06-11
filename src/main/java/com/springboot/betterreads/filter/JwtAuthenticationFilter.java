package com.springboot.betterreads.filter;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.springboot.betterreads.domain.AuthClaims;
import com.springboot.betterreads.domain.ErrorResponse;
import com.springboot.betterreads.helper.BetterReadsHelper;
import com.springboot.betterreads.utils.BetterReadsConstants;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	@Autowired
	private BetterReadsHelper betterReadsHelper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		logger.info("Inside JwtAuthenticationFilter doFilterInternal method.");
		String requestUri = request.getRequestURI();
		String authToken = request.getHeader(BetterReadsConstants.AUTHORIZATION);
		logger.info("Request URI : [{}]", requestUri);

		try {
			
			if(requestUri.contains(BetterReadsConstants.GET_BOOK_BY_ID_URI) && !StringUtils.hasLength(authToken)) {
				logger.info("Allowing for get_book_by_id without login.");
				filterChain.doFilter(request, response);
				return;
			}
			
			if (requestUri.contains(BetterReadsConstants.ACTUATOR_URI)) {
				logger.info("Allowing for actuator endpoints.");
				filterChain.doFilter(request, response);
				return;
			} else {
				logger.info("Validating token for other endpoints.");

				if (!StringUtils.hasLength(authToken)) {
					logger.error("Authorization token not present in header.");
					this.sendErrorResponse(request, response, HttpServletResponse.SC_UNAUTHORIZED);
					return;
				}

				if (!authToken.startsWith(BetterReadsConstants.BEARER)) {
					logger.error("Invalid authorization token.");
					this.sendErrorResponse(request, response, HttpServletResponse.SC_UNAUTHORIZED);
					return;
				}

				String tokenValue = authToken.split(BetterReadsConstants.SPACE_DELIMITER)[1];
				if (!StringUtils.hasLength(tokenValue)) {
					logger.error("Authorization token not present.");
					this.sendErrorResponse(request, response, HttpServletResponse.SC_UNAUTHORIZED);
					return;
				}
				
				AuthClaims authClaims = betterReadsHelper.validateTokenAndReturnClaims(authToken);
				List<String> authList = authClaims.getAuthorities();
				if (!CollectionUtils.isEmpty(authList) && authList.contains("TodoAdmin")) {
					filterChain.doFilter(request, response);
					return;
				}
				this.sendErrorResponse(request, response, HttpServletResponse.SC_FORBIDDEN);
				return;
			}
		} catch (Exception e) {
			this.sendErrorResponse(request, response, HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

	private void sendErrorResponse(HttpServletRequest request, HttpServletResponse response, int statusCode)
			throws IOException {
		logger.info("Inside JwtAuthenticationFilter sendErrorResponse method.");
		response.setContentType(BetterReadsConstants.CONTENT_TYPE_JSON);
		response.setStatus(statusCode);
		response.getWriter().write(this.getErrorResponse(request, statusCode));
	}

	private String getErrorResponse(HttpServletRequest request, int statusCode) {
		logger.info("Inside JwtAuthenticationFilter getErrorResponse method.");
		try {
			String errorMessage = BetterReadsConstants.JWT_TOKEN_VALIDATION_FAILURE_MSG;
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setErrorMessage(errorMessage);
			errorResponse.setErrorCode(String.valueOf(statusCode));
			errorResponse.setDebugMessage(errorMessage);
			errorResponse.setPath(request.getRequestURI());
			errorResponse.setTimestamp(Instant.now().toEpochMilli());

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

			return objectMapper.writeValueAsString(errorResponse);
		} catch (JsonProcessingException e) {
			logger.error("Error occurred in JwtAuthenticationFilter getErrorResponse method.", e);
		}
		return null;
	}
}
