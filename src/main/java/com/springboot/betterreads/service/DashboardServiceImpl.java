package com.springboot.betterreads.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.springboot.betterreads.domain.BooksByUser;
import com.springboot.betterreads.exception.BadRequestException;
import com.springboot.betterreads.exception.RecordNotFoundException;
import com.springboot.betterreads.repository.BooksByUserRepository;
import com.springboot.betterreads.utils.BetterReadsConstants;

@Service
public class DashboardServiceImpl implements DashboardService {

	private Logger logger = LoggerFactory.getLogger(DashboardServiceImpl.class);

	@Autowired
	private BooksByUserRepository booksByUserRepository;

	@Override
	public List<BooksByUser> getBooksByUser(String userId) throws BadRequestException, RecordNotFoundException {
		// get only recent 15 books for user
		if (!StringUtils.hasLength(userId)) {
			String errorMessage = "User ID is empty or null, cannot fetch books for dashbaord.";
			logger.error(errorMessage);
			throw new BadRequestException(errorMessage, errorMessage, HttpStatus.BAD_REQUEST.toString());
		}
		Slice<BooksByUser> booksSlice = booksByUserRepository.findAllById(userId, CassandraPageRequest.of(0, 100));
		List<BooksByUser> booksByUser = booksSlice.getContent();
		if (!booksSlice.hasContent()) {
			String errorMessage = String.format("No books found for user with ID : %s", userId);
			throw new RecordNotFoundException(errorMessage, errorMessage, HttpStatus.NOT_FOUND.toString());
		}
		// distinct will fetch unique records based on hashcode and equals and if there
		// is a UserDefined Bean then override hashcode and equals so that distinct will
		// work automatically
		//filter based on reading status and return only one record based on bookId
		List<BooksByUser> books = booksByUser.stream().distinct().map(book -> {
			String coverImageUrl = "/src/main/resources/images/no-image.png";
			if (!CollectionUtils.isEmpty(book.getCoverIds())) {
				coverImageUrl = BetterReadsConstants.COVER_IMAGE_URL + book.getCoverIds().get(0) + "-L.jpg";
			}
			book.setCoverUrl(coverImageUrl);
			return book;
		}).collect(Collectors.toList());

		return books;
	}

	@Override
	public BooksByUser saveBooksByUser(BooksByUser booksByUser) throws BadRequestException {
		if (booksByUser == null) {
			String errorMessage = "Book details not present, cannot save books details.";
			logger.error(errorMessage);
			throw new BadRequestException(errorMessage, errorMessage, HttpStatus.BAD_REQUEST.toString());
		}

		return booksByUserRepository.save(booksByUser);
	}
}
