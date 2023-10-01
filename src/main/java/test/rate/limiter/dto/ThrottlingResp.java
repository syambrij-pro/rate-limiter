package test.rate.limiter.dto;

import org.springframework.http.HttpStatus;

/**
 * @author syambrij
 *
 */
public class ThrottlingResp {
	
	/**
	 * Status of returned response. 
	 */
	private HttpStatus httpStatus;
	
	/**
	 * Will be returned true if limiter allows the request;
	 */
	private boolean allowed;
	
	/**
	 * Data is returned only in cases where httpStatus is 429.
	 * It is used by client to retry after this much time.
	 * 
	 * Returned in MILLISECONDS.
	 */
	private int timeToWait;
	
	private ThrottlingReq relatedRequestDTO;
	
	public ThrottlingResp() {
	}

	public ThrottlingResp(HttpStatus httpStatus, boolean allowed, int timeToWait, ThrottlingReq relatedRequestDTO) {
		this.httpStatus = httpStatus;
		this.allowed = allowed;
		this.timeToWait = timeToWait;
		this.relatedRequestDTO = relatedRequestDTO;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public boolean isAllowed() {
		return allowed;
	}

	public void setAllowed(boolean allowed) {
		this.allowed = allowed;
	}

	public int getTimeToWait() {
		return timeToWait;
	}

	public void setTimeToWait(int timeToWait) {
		this.timeToWait = timeToWait;
	}

	public ThrottlingReq getRelatedRequestDTO() {
		return relatedRequestDTO;
	}

	public void setRelatedRequestDTO(ThrottlingReq relatedRequestDTO) {
		this.relatedRequestDTO = relatedRequestDTO;
	}

}
