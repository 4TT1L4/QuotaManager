package ch.quotamanager.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ch.quotamanager.model.ResponseString;
import ch.quotamanager.service.UploadChecker;
import ch.quotamanager.service.UploadChecker.Result;

/**
 * {@link RestController} for the /checkUpload endpoint. 
 */
@RestController
public class CheckUploadController {

	@Autowired
	UploadChecker checker;
	
	@PostMapping(value = "/checkUpload", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseString> checkUpload(
			Authentication authentication,
			@RequestParam("file") MultipartFile file
		) throws IOException {
		String userId = authentication.getName();
		long fileSize = file.getSize();
		
		Result result = checker.checkUpload(userId, file.getInputStream(), fileSize);
		if (result == Result.OK) {
			return new ResponseEntity<ResponseString>(HttpStatus.NO_CONTENT);
		}
		else {
			return new ResponseEntity<ResponseString>(ResponseString.create(result.toString()), HttpStatus.INSUFFICIENT_STORAGE);
		}
		
		
	}
}
