package com.springboot.betterreads.helper;

import java.util.Base64;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.springboot.betterreads.domain.BookVO;
import com.springboot.betterreads.domain.BooksByUser;
import com.springboot.betterreads.domain.UserBooksVO;
import com.springboot.betterreads.exception.BadRequestException;
import com.springboot.betterreads.service.DashboardServiceImpl;
import com.springboot.betterreads.service.UserBooksService;
import com.springboot.betterreads.utils.BetterReadsConstants;

@Component
public class BookHelper {

	private Logger logger = LoggerFactory.getLogger(BookHelper.class);

	@Autowired
	private UserBooksService userBooksServiceImpl;

	@Autowired
	private DashboardServiceImpl dashboardServiceImpl;

	public void setCoverImageUrl(BookVO bookVO) {
		String coverImageUrl = "/src/main/resources/images/no-image.png";
		if (!CollectionUtils.isEmpty(bookVO.getCoverIds())) {
			coverImageUrl = BetterReadsConstants.COVER_IMAGE_URL + bookVO.getCoverIds().get(0) + "-L.jpg";
			logger.info("Cover Image URL : {}", coverImageUrl);
			bookVO.setCoverImageUrl(coverImageUrl);
		} else {
			bookVO.setCoverImageUrl(coverImageUrl);
		}
	}

	public UserBooksVO fetchUserBookInfo(String authHeader, String bookId) throws BadRequestException {
		String userId = this.getUserIdFromToken(authHeader);
		UserBooksVO userBooksVO = userBooksServiceImpl.getUserBooks(userId, bookId);
		if (userBooksVO == null) {
			logger.info("Book information not found for userId : {} and bookId : {}", userId, bookId);
			//userBooksVO = new UserBooksVO();
		} else {
			logger.info("Book information found for userId : {} and bookId : {}", userId, bookId);
		}
		return userBooksVO;
	}

	public String getUserIdFromToken(String authHeader) throws BadRequestException {
		String accessToken = authHeader.split(" ")[1];
		String[] tokenValueArr = accessToken.split("\\.");
		String base64EncodedBody = tokenValueArr[1];
		String decodedToken = new String(Base64.getDecoder().decode(base64EncodedBody));
		JSONObject tokenJson = new JSONObject(decodedToken);
		if (tokenJson.has("sub")) {
			return tokenJson.getString("sub");
		}
		logger.error("User ID not present in token, cannot fetch books information for the logged in user.");
		throw new BadRequestException("User not found, cannot fetch books information for the logged in user.");
	}

	public void addBookByUser(UserBooksVO userBooksVO) {
		try {
			BooksByUser booksByUser = new BooksByUser();
			booksByUser.setId(userBooksVO.getKey().getUserId());
			booksByUser.setBookId(userBooksVO.getKey().getBookId());
			booksByUser.setBookName(userBooksVO.getName());
			booksByUser.setCoverIds(userBooksVO.getCoverIds());
			booksByUser.setAuthorNames(userBooksVO.getAuthorNames());
			booksByUser.setReadingStatus(userBooksVO.getReadingStatus());
			booksByUser.setRating(userBooksVO.getRating());
			BooksByUser savedBook = dashboardServiceImpl.saveBooksByUser(booksByUser);
			if (savedBook == null) {
				String errorMessage = String.format("Failed to save book %s for user %s", booksByUser.getBookId(),
						booksByUser.getId());
				logger.error(errorMessage);
			} else {
				logger.info("Book {} saved sucessfully for user : {}", booksByUser.getBookId(), booksByUser.getId());
			}

		} catch (BadRequestException e) {
			logger.error("BadRequestException : {},Failed to save book {} for user {}", e.getErrorMessage(),
					userBooksVO.getKey().getBookId(), userBooksVO.getKey().getUserId());
		} catch (Exception e) {
			logger.error("Exception : {},Failed to save book {} for user {}", e.getMessage(),
					userBooksVO.getKey().getBookId(), userBooksVO.getKey().getUserId());
		}
	}
}