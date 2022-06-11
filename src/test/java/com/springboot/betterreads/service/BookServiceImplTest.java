package com.springboot.betterreads.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.springboot.betterreads.domain.Book;
import com.springboot.betterreads.domain.BookVO;
import com.springboot.betterreads.exception.BadRequestException;
import com.springboot.betterreads.exception.RecordNotFoundException;
import com.springboot.betterreads.helper.BookHelper;
import com.springboot.betterreads.repository.BookRepository;

//@ExtendWith(SpringExtension.class)
//@SpringBootTest
public class BookServiceImplTest {

	@Autowired
	@InjectMocks
	private BookServiceImpl bookServiceImpl;

	@Mock
	private BookHelper bookHelper;

	@Mock
	private BookRepository bookRepository;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void getBookById_success() throws RecordNotFoundException, BadRequestException {
		String bookId = "test";
		Book book = this.getMockBook();
		Mockito.doNothing().when(bookHelper).setCoverImageUrl(Mockito.any(BookVO.class));
		Mockito.when(bookRepository.findById(Mockito.anyString())).thenReturn(Optional.of(book));
		BookVO bookVO = bookServiceImpl.getBookById(bookId, "Bearer test");
		assertNotNull(bookVO);
	}

	@Test
	public void getBookById_notFound() {
		String bookId = "test";
		Mockito.doNothing().when(bookHelper).setCoverImageUrl(Mockito.any(BookVO.class));
		Mockito.when(bookRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		assertThrows(RecordNotFoundException.class, () -> {
			bookServiceImpl.getBookById(bookId, "Bearer test");
		});
	}
	
	@Test
	public void getBookById_badRequest() {
		Mockito.doNothing().when(bookHelper).setCoverImageUrl(Mockito.any(BookVO.class));
		Mockito.when(bookRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		assertThrows(BadRequestException.class, () -> {
			bookServiceImpl.getBookById("", "Bearer test");
		});
	}

	private Book getMockBook() {
		Book book = new Book();
		return book;
	}
}
