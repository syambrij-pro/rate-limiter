package test.rate.limiter.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import test.rate.limiter.algorithm.LazyFillBucketLimiter;
import test.rate.limiter.algorithm.Limiter;
import test.rate.limiter.algorithm.SlidingLogLimiter;
import test.rate.limiter.algorithm.SlidingWindowLimiter;
import test.rate.limiter.algorithm.TokenBucketLimiter;
import test.rate.limiter.dto.ThrottlingReq;
import test.rate.limiter.dto.UserRequestKey;
import test.rate.limiter.repository.entity.LimitConfiguration;
import test.rate.limiter.repository.entity.RateLimitPolicy;

/**
 *
 * @author syambrij
 *
 */
@Service
public class RateLimitServiceImpl implements RateLimitService {
	
	@Autowired
	private ConfigurationService configurationService;
	
	private Map<UserRequestKey, Limiter> rateLimiterCache = new ConcurrentHashMap<>();
	
	public boolean processRequest(ThrottlingReq throttlingReq, LimitConfiguration limitConfig) {
		Limiter limiter = rateLimiterCache.get(new UserRequestKey(throttlingReq.getRequestUrl(), throttlingReq.getUserId()));
		if (limiter == null) {
			synchronized (this) {
				if (limiter == null) {
					limiter = createAndPutInCache(limitConfig);
				}
			}
		}
		return limiter.tryAcquire(throttlingReq);
	}
	
	private Limiter createAndPutInCache(LimitConfiguration limitConfig) {
		Limiter limiter = null;
		if (limitConfig.getPolicy().equals(RateLimitPolicy.LAZY_FILL_BUCKET)) {
			limiter = new LazyFillBucketLimiter(limitConfig);
		} else if (limitConfig.getPolicy().equals(RateLimitPolicy.SLIDING_WINDOW)) {
			limiter = new SlidingWindowLimiter(limitConfig);
		} else if (limitConfig.getPolicy().equals(RateLimitPolicy.SLIDING_LOG)) {
			limiter = new SlidingLogLimiter(limitConfig);
		} else if (limitConfig.getPolicy().equals(RateLimitPolicy.TOKEN_BUCKET)) {
			limiter = new TokenBucketLimiter(limitConfig);
		}
		rateLimiterCache.put(new UserRequestKey(limitConfig.getRequestUrl(), limitConfig.getUserId()), limiter);
		return limiter;
	}

	@Override
	public boolean processRequest(ThrottlingReq throttlingReq) {
		LimitConfiguration limitConfig = configurationService.findLimitConfiguration(throttlingReq.getRequestUrl(), throttlingReq.getUserId());
		if (limitConfig == null) {
			return true;
		}
		return processRequest(throttlingReq, limitConfig);
	}
	
}

