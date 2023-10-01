package rate.limiter.algorithm;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import rate.limiter.dto.ThrottlingReq;
import rate.limiter.repository.entity.LimitConfiguration;

/**
 * A custom sliding window limiter. Which supports granularity level of milliseconds.
 * 
 * Pros: 
 * 1. Easy to understand and implement in local or distributed system.
 * 
 * 2. A REDIS implementation will be easy with hashes using time stamp as key.
 *  
 * Cons:
 *      Large memory footprint + Computation overhead.
 * 
 * @author syambrij
 *
 */
public class SlidingWindowLimiter implements Limiter {
	
	private int limitPerMinute;
	
	private Map<Long, AtomicLong> completeOneMinuteMap = new ConcurrentHashMap<>();
	
	private AtomicLong totalCount = new AtomicLong(0l);
	
	private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
	private final Lock readLock = rwLock.readLock();
	private final Lock writeLock = rwLock.writeLock();
	
	public SlidingWindowLimiter(LimitConfiguration limitConfiguration) {
		this.limitPerMinute = limitConfiguration.getLimitPerMinute() == 0 ?
				DEFAULT_PER_USER_LIMIT_PER_MINUTE : limitConfiguration.getLimitPerMinute();
	}
	
	@Override
	public boolean tryAcquire(ThrottlingReq throttlingRequest) {
		long currentTime = System.currentTimeMillis();
		long currentKey = (currentTime /1000) * 1000;   // seconds
		if (totalCount.get() < limitPerMinute) {
			return simplyAllow(currentKey);
		} else {
			try {
				writeLock.lock();
				long oldKeyBoundary = currentKey - 60*1000;
				completeOneMinuteMap.entrySet().removeIf(entry -> {
					boolean toBeRemoved = entry.getKey() <= oldKeyBoundary;
					if (toBeRemoved) {
						totalCount.set(totalCount.get() - entry.getValue().get());
					}
					return toBeRemoved;
				});
				return totalCount.get() < limitPerMinute;
			} finally {
				writeLock.unlock();
			}
		}
	}

	private boolean simplyAllow(long currentKey) {
		try {
			readLock.lock();
			AtomicLong countPerSecond = completeOneMinuteMap.getOrDefault(currentKey, new AtomicLong(0));
			countPerSecond.incrementAndGet();
			completeOneMinuteMap.put(currentKey, countPerSecond);
			totalCount.incrementAndGet();
			return true;
		}  finally{
			readLock.unlock();
		}
	}

}
