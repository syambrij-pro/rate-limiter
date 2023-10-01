package rate.limiter.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import rate.limiter.dto.ThrottlingReq;
import rate.limiter.service.RateLimitService;

@RestController
public class ThrottlingController {
	
	@Autowired
	private RateLimitService rateLimitService;
	
	@PostMapping(path = "/checkLimit")
	public boolean checkThrottling(@RequestBody @Valid ThrottlingReq throttlingReq) {
		return rateLimitService.processRequest(throttlingReq);
	}

}
