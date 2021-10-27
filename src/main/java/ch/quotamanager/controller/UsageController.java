package ch.quotamanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.quotamanager.service.UsageInformationService;
import ch.quotamanager.service.UsageInformationService.UsageInformation;

/**
 * {@link RestController} for the /usage endpoint. 
 */
@RestController
public class UsageController {
	
	@Autowired
	private UsageInformationService usageInformation;

	@GetMapping("/usage")
	public UsageInformation getUsage(Authentication authentication) {
		String userId = authentication.getName();
		
		UsageInformation usageInfo = usageInformation.getUsageInfomationForUserWith(userId);
		return usageInfo;
	}
}
