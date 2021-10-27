package ch.quotamanager.controller;

import ch.quotamanager.model.ResponseString;
import ch.quotamanager.service.SubscriptionManager;
import ch.quotamanager.service.SubscriptionManager.Result;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@link RestController} for the /setSubscription endpoint. 
 */
@RestController
public class SubscriptionController {

	@Autowired
	SubscriptionManager subscriptionManager;
	
	@PostMapping(path= "/setSubscription", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseString> setSubscription(
				Authentication authentication,
				@RequestBody Map<String, String> newSubscriptionForUser
			) {

		Result result = subscriptionManager.setSubscription(
				newSubscriptionForUser.get("UserId"),
				newSubscriptionForUser.get("Subscription")
				);

		switch (result) {
		case OK:
			return new ResponseEntity<ResponseString>(HttpStatus.NO_CONTENT);
		case USER_NOT_FOUND:
			return new ResponseEntity<ResponseString>(HttpStatus.NOT_FOUND);
		case INVALID_USER_ID:
			return new ResponseEntity<ResponseString>(ResponseString.create("INVALID_USER_ID"), HttpStatus.BAD_REQUEST);
		case INVALID_SUBSCRIPTION_TYPE:
			return new ResponseEntity<ResponseString>(ResponseString.create("INVALID_SUBSCRIPTION_TYPE"), HttpStatus.BAD_REQUEST);
		default:
			return new ResponseEntity<ResponseString>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
