package com.springboot.betterreads.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.springboot.betterreads.domain.AuthClaims;
import com.springboot.betterreads.domain.BookVO;
import com.springboot.betterreads.helper.BetterReadsHelper;
import com.springboot.betterreads.service.BookServiceImpl;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {

	@MockBean
	private BookServiceImpl bookServiceImpl;

	@MockBean
	private BetterReadsHelper helper;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testGetBookById_success() throws Exception {
		String bookId = "test";
		BookVO book = this.getMockBook(bookId);
		AuthClaims mockClaims = this.getMockClaims();
		when(helper.validateTokenAndReturnClaims(Mockito.anyString())).thenReturn(mockClaims);
		when(bookServiceImpl.getBookById(Mockito.anyString(), Mockito.anyString())).thenReturn(book);
		mockMvc.perform(get("/better-reads/books/{bookId}", bookId).header("authorization", "Bearer test"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testGetBookById_success_noAuth() throws Exception {
		String bookId = "test";
		BookVO book = this.getMockBook(bookId);
		when(bookServiceImpl.getBookById(Mockito.anyString(), Mockito.anyString())).thenReturn(book);
		mockMvc.perform(get("/better-reads/books/{bookId}", bookId)).andExpect(MockMvcResultMatchers.status().isOk());
	}

	private BookVO getMockBook(String bookId) {
		BookVO bookVO = new BookVO();
		return bookVO;
	}
	
	private AuthClaims getMockClaims() {
		AuthClaims claims = new AuthClaims();
		claims.setAuthorities(List.of("TodoAdmin"));
		claims.setSubject("varunpadaki");
		return claims;
	}

}
