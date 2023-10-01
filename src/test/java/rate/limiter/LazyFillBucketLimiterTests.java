package rate.limiter;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import rate.limiter.algorithm.LazyFillBucketLimiter;
import rate.limiter.algorithm.Limiter;
import rate.limiter.dto.ThrottlingReq;
import rate.limiter.repository.entity.LimitConfiguration;
import rate.limiter.repository.entity.RateLimitPolicy;

@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class LazyFillBucketLimiterTests extends AbstractRateLimiterTests {
	
	@Test
	public void testBucketWorking() {
		LimitConfiguration limitConfig = createLimitConfig("LAZY_FILL_USER", "test1", RateLimitPolicy.LAZY_FILL_BUCKET);
		Limiter bucketLimiter = new LazyFillBucketLimiter(limitConfig);
		ThrottlingReq throttlingReq = createThrottlingReq("LAZY_FILL_USER", "test1");
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
	public void testBucketWorkingWithSleep() throws InterruptedException {
		LimitConfiguration limitConfig = createLimitConfig("LAZY_FILL_USER_SLEEP", "test1", RateLimitPolicy.LAZY_FILL_BUCKET);
		Limiter bucketLimiter = new LazyFillBucketLimiter(limitConfig);
		ThrottlingReq throttlingReq = createThrottlingReq("LAZY_FILL_USER_SLEEP", "test1");
		int trueCount = 0, falseCount = 0;
		for (int i = 0; i < 300; i++) {
			if (bucketLimiter.tryAcquire(throttlingReq)) {
				trueCount++;
			} else {
				falseCount++;
			}
		}
		TimeUnit.MINUTES.sleep(1);
		for (int i = 0; i < 6; i++) {
			if (bucketLimiter.tryAcquire(throttlingReq)) {
				trueCount++;
			}
		}
		assert trueCount == 100 + 6;
		assert falseCount > trueCount;
	}
	
	@Test
	public void testLazyFillBucketInterval() throws InterruptedException {
		LimitConfiguration limitConfig = createLimitConfig("LAZY_FILL_USER", "test1", RateLimitPolicy.LAZY_FILL_BUCKET);
		Limiter bucketLimiter = new LazyFillBucketLimiter(limitConfig);
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
