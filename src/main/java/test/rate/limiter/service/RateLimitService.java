package test.rate.limiter.service;

import test.rate.limiter.dto.ThrottlingReq;

public interface RateLimitService {

	boolean processRequest(ThrottlingReq throttlingRequest);

}
