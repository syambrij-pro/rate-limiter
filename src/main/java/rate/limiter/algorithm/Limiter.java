package rate.limiter.algorithm;

import rate.limiter.dto.ThrottlingReq;

public interface Limiter {
	
	/**
	 * API limit across all users. If not mentioned particularly for user. 
	 * 
	 */
	public static final int DEFAULT_GLOBAL_LIMIT_PER_MINUTE = 6000; 
	
	/**
	 * Per user default limit per minute for any API endpoint.
	 */
	public static final int DEFAULT_PER_USER_LIMIT_PER_MINUTE = 600;
	
	public boolean tryAcquire(ThrottlingReq throttlingReq);

}
