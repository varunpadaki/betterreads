package com.springboot.betterreads.helper;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.betterreads.domain.AuthClaims;
import com.springboot.betterreads.exception.BetterReadsAuthException;
import com.springboot.betterreads.utils.BetterReadsConstants;

@Component
public class BetterReadsHelper {

	@Value("${betterreads.token.validate.uri}")
	private String tokenValidateUri;

	private Logger logger = LoggerFactory.getLogger(BetterReadsHelper.class);

	public AuthClaims validateTokenAndReturnClaims(String authToken) throws BetterReadsAuthException {
		String errorMessage = "";
		try {
			Map<String, Object> pathVariableMap = new HashMap<String, Object>();
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			httpHeaders.setBearerAuth(authToken.split(BetterReadsConstants.SPACE_DELIMITER)[1]);
			HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.exchange(tokenValidateUri, HttpMethod.POST, httpEntity,
					String.class, pathVariableMap);
			if (response != null && response.getStatusCode().is2xxSuccessful()) {
				logger.info("Auth token validated successfully, returning claims.");
				String authClaimStr = response.getBody();
				ObjectMapper mapper = new ObjectMapper();
				return mapper.readValue(authClaimStr, AuthClaims.class);
			}
			errorMessage = "Failed to validate auth token for better reads.";
		} catch (JsonProcessingException e) {
			logger.error("JsonProcessing exception occurred,failed to validate auth token", e);
			errorMessage = e.getMessage();
		} catch (Exception e) {
			logger.error("Error occurred,failed to validate auth token", e);
			errorMessage = e.getMessage();
		}
		throw new BetterReadsAuthException(errorMessage);
	}
}
