package rate.limiter.dto;

import java.util.Objects;

public class UserRequestKey {
	
	private String userId;
	private String requestUrl;
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getRequestUrl() {
		return requestUrl;
	}
	
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	
	public UserRequestKey(String requestUrl, String userId) {
		this.requestUrl = requestUrl;
		this.userId = userId;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(requestUrl, userId);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserRequestKey other = (UserRequestKey) obj;
		return Objects.equals(requestUrl, other.requestUrl) && Objects.equals(userId, other.userId);
	}
	
}
