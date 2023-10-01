package rate.limiter.algorithm;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import rate.limiter.dto.ThrottlingReq;
import rate.limiter.repository.entity.LimitConfiguration;

/**
 * This class implements rate limiter using token bucket algorithm.
 * A scheduled executer thread is used to cleanup bucket at every second.
 * 
 * @author syambrij
 *
 */
public class TokenBucketLimiter implements Limiter {

	private static ScheduledExecutorService scheduler = null; // for leaky bucket refresh.

	private static final int DEFAULT_GLOBAL_LIMIT_PER_MINUTE = 6000;

	private static final int DEFAULT_GLOBAL_BUCKET_REFRESH_WINDOW = 1000; // Thousand milliseconds.

	private static final int DEFAULT_TIMEOUT = 10_000; // 10 seconds.

	private Semaphore semaphore;

	private int limitPerMinute;

	private int leakRate;

	static {
		scheduler = Executors.newScheduledThreadPool(4, (runnable) -> {
			Thread thread = new Thread(runnable);
			thread.setDaemon(true);
			return thread;
		});
	}

	public TokenBucketLimiter(LimitConfiguration limitConfiguration) {
		this.limitPerMinute = limitConfiguration.getLimitPerMinute() == 0 ? DEFAULT_GLOBAL_LIMIT_PER_MINUTE
				: limitConfiguration.getLimitPerMinute();
		this.semaphore = new Semaphore(limitPerMinute);
		scheduler.scheduleAtFixedRate(this::releaseForOneWindow, 100, DEFAULT_GLOBAL_BUCKET_REFRESH_WINDOW,
				TimeUnit.MILLISECONDS);
		leakRate = limitPerMinute / 60 + 1;
	}

	public void releaseForOneWindow() {
		semaphore.release(leakRate);
		if (semaphore.availablePermits() > limitPerMinute) {
			semaphore = new Semaphore(limitPerMinute);
		}
	}

	@Override
	public boolean tryAcquire(ThrottlingReq throttlingRequest) {
		int timeOutInNanos = throttlingRequest.getTimeout() == 0 ? DEFAULT_TIMEOUT * 1_000_000
				: throttlingRequest.getTimeout() * 1_000_000;
		try {
			return semaphore.tryAcquire(1, timeOutInNanos, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return false;
		}
	}

}
