package rate.limiter.service;

import rate.limiter.dto.ThrottlingReq;

public interface RateLimitService {

	boolean processRequest(ThrottlingReq throttlingRequest);

}
