package test.rate.limiter.dto;

import java.util.List;

import org.springframework.http.HttpStatus;

public class ErrorResp {
	
	private HttpStatus httpStatus;
	
	private String reason;
	
	private List<String> errorMessages;

	public ErrorResp(HttpStatus httpStatus, String reason, List<String> errorMessages) {
		this.httpStatus = httpStatus;
		this.reason = reason;
		this.errorMessages = errorMessages;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public List<String> getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}
}
