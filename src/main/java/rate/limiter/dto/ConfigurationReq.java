package rate.limiter.dto;

import javax.validation.constraints.NotEmpty;

public class ConfigurationReq {
	
	@NotEmpty(message = "requestUrl must not be empty")
	private String requestUrl;
	
	/**
	 * Could be user_name, user_id, api_key or IP_ADDRESS. 
	 * In case of null it can be assumed that this is global limit.
	 */
	private String userId;
	
	/**
	 * Should be configured as per TPS expected and system resource capacity.
	 */
	private int limitPerMinute;
	
	/**
	 * Choose policy for RateLimiting.
	 */
	private String rateLimitPolicy;
	
	public ConfigurationReq() {
	}

	public ConfigurationReq(@NotEmpty(message = "requestUrl must not be empty") String requestUrl, String userId,
			int limitPerMinute, String rateLimitPolicy) {
		this.requestUrl = requestUrl;
		this.userId = userId;
		this.limitPerMinute = limitPerMinute;
		this.rateLimitPolicy = rateLimitPolicy;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getLimitPerMinute() {
		return limitPerMinute;
	}

	public void setLimitPerMinute(int limitPerMinute) {
		this.limitPerMinute = limitPerMinute;
	}

	public String getRateLimitPolicy() {
		return rateLimitPolicy;
	}

	public void setRateLimitPolicy(String rateLimitPolicy) {
		this.rateLimitPolicy = rateLimitPolicy;
	}

	@Override
	public String toString() {
		return "ConfigurationReq [requestUrl=" + requestUrl + ", userId=" + userId + ", limitPerMinute="
				+ limitPerMinute + ", rateLimitPolicy=" + rateLimitPolicy + "]";
	}
	
}
