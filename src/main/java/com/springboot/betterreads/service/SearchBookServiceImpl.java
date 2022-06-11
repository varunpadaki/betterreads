package com.springboot.betterreads.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.springboot.betterreads.domain.BookSearchResult;
import com.springboot.betterreads.domain.SearchBookResult;
import com.springboot.betterreads.exception.BadRequestException;
import com.springboot.betterreads.exception.RecordNotFoundException;
import com.springboot.betterreads.utils.BetterReadsConstants;

import reactor.core.publisher.Mono;

@Service
public class SearchBookServiceImpl implements SearchBookService {

	private Logger logger = LoggerFactory.getLogger(SearchBookServiceImpl.class);

	private WebClient webClient;

	public SearchBookServiceImpl(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder
				.exchangeStrategies(ExchangeStrategies.builder()
						.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)).build())
				.baseUrl("http://openlibrary.org/search.json").build();
	}

	@Override
	public List<SearchBookResult> searchBooks(String query) throws RecordNotFoundException, BadRequestException {

		if (!StringUtils.hasLength(query)) {
			String errorMessage = "Query param is empty or null, cannot search books.";
			logger.error(errorMessage);
			// set custom error code if defined else set httpstatus code
			throw new BadRequestException(errorMessage, errorMessage, HttpStatus.BAD_REQUEST.toString());
		}

		Mono<BookSearchResult> resultsMono = this.webClient.get().uri("?q={query}", query).retrieve()
				.bodyToMono(BookSearchResult.class);
		BookSearchResult bookSearchResult = resultsMono.block();
		if (bookSearchResult == null || bookSearchResult.getNumFound() == 0
				|| CollectionUtils.isEmpty(bookSearchResult.getDocs())) {
			String errorMessage = String.format("Books not found for given query parameter : {}", query);
			throw new RecordNotFoundException(errorMessage, errorMessage, HttpStatus.NOT_FOUND.toString());
		}

		logger.info("Books found for query parameter : {}", query);
		logger.info("Total books found = {}", bookSearchResult.getNumFound());
		List<SearchBookResult> books = bookSearchResult.getDocs().stream().limit(10).map(bookResult -> {
			bookResult.setKey(bookResult.getKey().replace("/works/", ""));
			String coverId = bookResult.getCover_i();
			if (StringUtils.hasLength(coverId)) {
				coverId = BetterReadsConstants.COVER_IMAGE_URL + coverId + "-M.jpg";
			} else {
				coverId = "/src/main/resources/images/no-image.png";
			}
			bookResult.setCover_i(coverId);
			return bookResult;
		}).collect(Collectors.toList());
		return books;
	}
}
