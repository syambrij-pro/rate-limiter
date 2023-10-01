package test.rate.limiter.repository.entity;

/**
 * @author syambrij
 *
 */
public enum RateLimitPolicy {
	
	TOKEN_BUCKET("For global throttling per request urls."),
	LAZY_FILL_BUCKET("For global throttling per request urls."),
	SLIDING_LOG("User + RequestURL"),
	SLIDING_WINDOW("User + RequestURL");
	
	private String description;
	
	private RateLimitPolicy(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
