package test.rate.limiter;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import test.rate.limiter.dto.ThrottlingReq;
import test.rate.limiter.repository.entity.LimitConfiguration;
import test.rate.limiter.repository.entity.RateLimitPolicy;
import test.rate.limiter.service.Limiter;
import test.rate.limiter.service.SlidingLogLimiter;

@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class SlidingLogLimiterTests extends AbstractRateLimiterTests {
	
	@Test
	public void testSlidingWimdow() {
		LimitConfiguration limitConfig = createLimitConfig("SLIDING_LOG_USER", "test1", RateLimitPolicy.SLIDING_WINDOW);
		Limiter bucketLimiter = new SlidingLogLimiter(limitConfig);
		ThrottlingReq throttlingReq = createThrottlingReq("SLIDING_LOG_USER", "test1");
		int trueCount = 0, falseCount = 0;
		for (int i = 0; i < 300; i++) {
			if (bucketLimiter.tryAcquire(throttlingReq)) {
				trueCount++;
			} else {
				falseCount++;
			}
		}
		assert falseCount > trueCount;
	}
	
	@Test
	public void testSlidingWimdowWithSleep() throws InterruptedException {
		LimitConfiguration limitConfig = createLimitConfig("SLIDING_LOG_USER_SLEEP", "test1", RateLimitPolicy.SLIDING_WINDOW);
		ThrottlingReq throttlingReq = createThrottlingReq("SLIDING_LOG_USER_SLEEP", "test1");
		Limiter bucketLimiter = new SlidingLogLimiter(limitConfig);
		int trueCount = 0;
		for (int i = 0; i < 150; i++) {
			if (bucketLimiter.tryAcquire(throttlingReq)) {
				trueCount++;
			}
		}
		TimeUnit.MINUTES.sleep(1);
		for (int i = 0; i < 6; i++) {
			if (bucketLimiter.tryAcquire(throttlingReq)) {
				trueCount++;
			}
		}
		assert trueCount == 100 + 6;
	}
	
	@Test
	public void testLazyFillBucketInterval() throws InterruptedException {
		LimitConfiguration limitConfig = createLimitConfig("LAZY_FILL_USER", "test1", RateLimitPolicy.LAZY_FILL_BUCKET);
		Limiter bucketLimiter = new SlidingLogLimiter(limitConfig);
		ThrottlingReq throttlingReq = createThrottlingReq("LAZY_FILL_USER", "test1");
		int trueCount = 0, falseCount = 0;
		for (int i = 0; i < 20; i++) {
			if (bucketLimiter.tryAcquire(throttlingReq)) {
				trueCount++;
			} else {
				falseCount++;
			}
		}
		TimeUnit.SECONDS.sleep(20);
		for (int i = 0; i < 40; i++) {
			if (bucketLimiter.tryAcquire(throttlingReq)) {
				trueCount++;
			} else {
				falseCount++;
			}
		}
		TimeUnit.SECONDS.sleep(20);
		for (int i = 0; i < 40; i++) {
			if (bucketLimiter.tryAcquire(throttlingReq)) {
				trueCount++;
			} else {
				falseCount++;
			}
		}
		TimeUnit.SECONDS.sleep(21);
		for (int i = 0; i < 20; i++) {
			if (bucketLimiter.tryAcquire(throttlingReq)) {
				trueCount++;
			} else {
				falseCount++;
			}
		}
		TimeUnit.SECONDS.sleep(22);
		for (int i = 0; i < 20; i++) {
			if (bucketLimiter.tryAcquire(throttlingReq)) {
				trueCount++;
			} else {
				falseCount++;
			}
		}
		assert falseCount == 0;
		assert trueCount > 100; //greater than initial perMinuteLimit
	}
}
