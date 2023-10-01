package test.rate.limiter.service;

import java.util.concurrent.atomic.AtomicLong;

import test.rate.limiter.dto.ThrottlingReq;
import test.rate.limiter.repository.entity.LimitConfiguration;

/**
 * @author syambrij
 *
 */
public class LazyFillBucketLimiter implements Limiter {

	private AtomicLong tokencount;

	private int limitPerMinute;

	private int leakRate;

	private long lastRefillTime;

	public LazyFillBucketLimiter(LimitConfiguration limitConfiguration) {
		this.limitPerMinute = limitConfiguration.getLimitPerMinute() == 0 ? DEFAULT_GLOBAL_LIMIT_PER_MINUTE
				: limitConfiguration.getLimitPerMinute();
		this.tokencount = new AtomicLong(limitPerMinute);
		leakRate = (limitPerMinute / 60) + 1;
		this.lastRefillTime = System.currentTimeMillis();
	}

	private void refillTokens() {
		synchronized (this) {
			long currentTime = System.currentTimeMillis();
			double secondsPassedInBetween = (currentTime - lastRefillTime) / 1000.0;
			long count = (long) (secondsPassedInBetween * leakRate);
			count = Math.min(count, limitPerMinute - tokencount.get());
			tokencount.set(tokencount.get() - count);
			lastRefillTime = currentTime;
		}
	}

	@Override
	public boolean tryAcquire(ThrottlingReq throttlingRequest) {
		refillTokens();
		if (tokencount.get() == 0) {
			return false;
		}
		tokencount.decrementAndGet();
		return true;
	}
}