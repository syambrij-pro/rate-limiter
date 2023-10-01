package test.rate.limiter.algorithm;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

import test.rate.limiter.dto.ThrottlingReq;
import test.rate.limiter.repository.entity.LimitConfiguration;

/**
 * A custom sliding log limiter. Which supports granularity level of milliseconds.
 * 
 * Pros: 
 * 1. Easy to understand and implement in local or distributed system.
 * 
 * 2. A REDIS implementation will be easy with sorted set.
 *    Where scores are simply long values of time stamp.
 *    
 *    A simple atomic operation ZREMRANGEBYSCORE in redis on sorted set will simply remove the
 *    old time stamps.
 *  
 * Cons:
 *      Higher memory foot print. 
 * 
 * @author syambrij
 *
 */
public class SlidingLogLimiter implements Limiter {
	
	private final int limitPerMinute;

	private final Queue<Long> slidingLog = new LinkedList<>();
	
	private ReentrantLock lock = new ReentrantLock();

	public SlidingLogLimiter(LimitConfiguration limitConfiguration) {
		this.limitPerMinute = limitConfiguration.getLimitPerMinute() == 0 ?
				DEFAULT_PER_USER_LIMIT_PER_MINUTE : limitConfiguration.getLimitPerMinute();
	}

	@Override
	public boolean tryAcquire(ThrottlingReq throttlingRequest) {
		long currentTime = System.currentTimeMillis();
		try {
			lock.lock();
			removeOldTimeStamps(currentTime - 60_000); //remove data before last one minute.
			slidingLog.add(currentTime);
			return slidingLog.size() <= limitPerMinute;
		} finally {
			lock.unlock();
		}
	}

	private void removeOldTimeStamps(long windowStart) {
		while (!slidingLog.isEmpty() && slidingLog.peek() <= windowStart) {
			slidingLog.poll();
		}
	}

}