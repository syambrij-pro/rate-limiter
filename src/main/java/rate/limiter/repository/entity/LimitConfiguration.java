package rate.limiter.repository.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "limit_configuration")
public class LimitConfiguration implements Serializable {

	private static final long serialVersionUID = 8381625585517946456L;
	
	@Id
    @GeneratedValue
    @Column(name = "id")
    private UUID configId;

    @Column(name = "request_url")
	private String requestUrl;
	
    @Column(name = "user_id")
	private String userId;  
	
    @Column(name = "limit_per_minute")
	private int limitPerMinute;
	
    @Column(name = "policy")
    @Enumerated(EnumType.STRING)
	private RateLimitPolicy policy;

	public UUID getConfigId() {
		return configId;
	}

	public void setConfigId(UUID configId) {
		this.configId = configId;
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

	public RateLimitPolicy getPolicy() {
		return policy;
	}

	public void setPolicy(RateLimitPolicy policy) {
		this.policy = policy;
	}
    
}
