package ch.quotamanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.util.ResourceUtils;

import ch.quotamanager.model.Subscription;
import ch.quotamanager.model.Usage;
import ch.quotamanager.model.User;
import ch.quotamanager.repository.UsageRepository;
import ch.quotamanager.repository.UserRepository;
import ch.quotamanager.service.UploadChecker;
import ch.quotamanager.service.UsageInformationService;
import ch.quotamanager.service.UploadChecker.Result;

/**
 * Tests the {@link UsageInformationService} class.
 */
public class UploadCheckerTest {

	@Mock
	UsageRepository usageRepository;

	@Mock
	UserRepository userRepository;

	// Object under test:
	@InjectMocks
	private UploadChecker uploadChecker;

	@BeforeEach
	void beforeEach() {
		MockitoAnnotations.initMocks(this);
		Mockito.when(userRepository.getUser("test")).thenReturn(new User("test", Subscription.LIGHT));
		Mockito.when(usageRepository.getUserUsage("test")).thenReturn(new Usage("test", 0, 0));
	}
	
	@Test
	void test_checkUpload_file_OK() {
		setSubscription(Subscription.LIGHT);
		setFileIncreaseSuccess(true);
		setPhotoIncreaseSuccess(true);

		InputStream input = asInputStream("Not a photo.");

		Result result = uploadChecker.checkUpload("test", input, 3);
		assertEquals(Result.OK, result);
	}

	@Test
	void test_checkUpload_file_noSpace() {
		setSubscription(Subscription.LIGHT);
		setFileIncreaseSuccess(false);
		setPhotoIncreaseSuccess(true);

		InputStream input = asInputStream("Not a photo.");

		Result result = uploadChecker.checkUpload("test", input, 3);
		assertEquals(Result.QUOTA_EXCEEDED, result);
	}

	@Test
	void test_checkUpload_file_overTheFileUploadLimit() {
		setSubscription(Subscription.LIGHT);
		setFileIncreaseSuccess(false);
		setPhotoIncreaseSuccess(true);

		InputStream input = asInputStream("DummyString");

		Result result = uploadChecker.checkUpload("test", input, Subscription.LIGHT.getUploadSizeLimit() + 1);
		assertEquals(Result.MAX_UPLOAD_SIZE_VIOLATED, result);
	}
	
	@Test
	void test_checkUpload_photo_overTheSizeLimit() throws IOException {
		setSubscription(Subscription.LIGHT);
		setFileIncreaseSuccess(true);
		setPhotoIncreaseSuccess(true);

		File file = ResourceUtils.getFile("classpath:photo.png");
		try (var input = new FileInputStream(file)) {
			Result result = uploadChecker.checkUpload("test", input, Subscription.LIGHT.getUploadSizeLimit() + 1);
			assertEquals(Result.MAX_UPLOAD_SIZE_VIOLATED, result);
		}
	}
	
	@Test
	void test_checkUpload_photo_ok() throws IOException {
		setSubscription(Subscription.LIGHT);
		setFileIncreaseSuccess(true);
		setPhotoIncreaseSuccess(true);

		File file = ResourceUtils.getFile("classpath:photo.png");
		try (var input = new FileInputStream(file)) {
			Result result = uploadChecker.checkUpload("test", input, 3);
			assertEquals(Result.OK, result);
		}
	}
	
	@Test
	void test_checkUpload_photo_noSpace() throws IOException {
		setSubscription(Subscription.LIGHT);
		setFileIncreaseSuccess(true);
		setPhotoIncreaseSuccess(false);

		File file = ResourceUtils.getFile("classpath:photo.png");
		try (var input = new FileInputStream(file)) {
			Result result = uploadChecker.checkUpload("test", input, 3);
			assertEquals(Result.QUOTA_EXCEEDED, result);
		}
	}
	
	@Test
	void test_checkUpload_photo_noSpaceButNoLimitForPhotos() throws IOException {
		setSubscription(Subscription.PRO);
		setFileIncreaseSuccess(true);
		setPhotoIncreaseSuccess(false);

		File file = ResourceUtils.getFile("classpath:photo.png");
		try (var input = new FileInputStream(file)) {
			Result result = uploadChecker.checkUpload("test", input, 3);
			assertEquals(Result.OK, result);
		}
	}

	private void setFileIncreaseSuccess(boolean success) {
		Mockito.when(usageRepository.increaseFileUsage(any(String.class), any(Long.class))).thenReturn(true); // no limit -> always success.
		Mockito.when(usageRepository.increaseFileUsage(any(String.class), any(Long.class), any(Long.class))).thenReturn(success);
	}

	private void setPhotoIncreaseSuccess(boolean success) {
		Mockito.when(usageRepository.increasePhotoUsage(any(String.class), any(Long.class))).thenReturn(true); // no limit -> always success.
		Mockito.when(usageRepository.increasePhotoUsage(any(String.class), any(Long.class), any(Long.class))).thenReturn(success);
	}
	
	private void setSubscription(Subscription subscription) {
		Mockito.when(userRepository.getUser("test")).thenReturn(new User("test", subscription));
	}

	private InputStream asInputStream(String s) {
		return new ByteArrayInputStream(s.getBytes(Charset.forName("UTF-8")));
	}
}
