package test.rate.limiter;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import test.rate.limiter.dto.ThrottlingReq;
import test.rate.limiter.repository.entity.LimitConfiguration;
import test.rate.limiter.repository.entity.RateLimitPolicy;

@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
abstract class AbstractRateLimiterTests {
	
	public LimitConfiguration createLimitConfig(String username, String url, RateLimitPolicy limitPolicy) {
		LimitConfiguration limitConfig = new LimitConfiguration();
		limitConfig.setRequestUrl(url);
		limitConfig.setUserId(username);
		limitConfig.setLimitPerMinute(100);
		limitConfig.setPolicy(limitPolicy);
		return limitConfig;
	}
	
	public ThrottlingReq createThrottlingReq(String username, String url) {
		ThrottlingReq throttlingReq = new ThrottlingReq();
		throttlingReq.setRequestUrl(url);
		throttlingReq.setUserId(username);
		throttlingReq.setTimeout(10000);
		return throttlingReq;
	}

}