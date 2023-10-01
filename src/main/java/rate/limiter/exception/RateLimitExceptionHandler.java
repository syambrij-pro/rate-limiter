package rate.limiter.exception;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import rate.limiter.dto.ErrorResp;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RateLimitExceptionHandler {

	private final Logger log = LoggerFactory.getLogger(RateLimitExceptionHandler.class);

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public @ResponseBody ErrorResp handleValidationExceptions(MethodArgumentNotValidException exception) {
		List<String> errorMessages = exception.getBindingResult().getAllErrors().stream()
				.map(error -> error.getDefaultMessage()).collect(Collectors.toList());
		log.error("Bad request received. Details: ", exception);
		ErrorResp errorResponse = new ErrorResp(HttpStatus.BAD_REQUEST, "Incorrect request data.", errorMessages);
		return errorResponse;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ ConstraintViolationException.class })
	public @ResponseBody ErrorResp handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
		List<String> errorMessages = ex.getConstraintViolations().stream()
				.map(violation -> violation.getPropertyPath().toString() + " " + violation.getMessage())
				.collect(Collectors.toList());
		log.error("Bad request received. Details: ", ex);
		ErrorResp errorResponse = new ErrorResp(HttpStatus.BAD_REQUEST, "Incorrect request data.", errorMessages);
		return errorResponse;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ HttpMessageNotReadableException.class })
	public @ResponseBody ErrorResp handleMethodArgumentTypeMismatch(HttpMessageNotReadableException ex,
			WebRequest request) {
		log.error("Invalid request type: " + ex.getHttpInputMessage(), ex);
		return new ErrorResp(HttpStatus.BAD_REQUEST, "Incorrect request data.",
				Collections.singletonList(ex.getMessage()));
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ DataIntegrityViolationException.class })
	public @ResponseBody ErrorResp handleMethodArgumentTypeMismatch(DataIntegrityViolationException ex,
			WebRequest request) {
		log.error("Invalid request type: " + ex.getMessage(), ex);
		return new ErrorResp(HttpStatus.BAD_REQUEST, "Incorrect request data.",
				Collections.singletonList(ex.getMessage()));
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public @ResponseBody ErrorResp requestHandlingNoHandlerFound(NoHandlerFoundException ex, WebRequest request) {
		log.error("WebRequest with URL not found.", ex);
		return new ErrorResp(HttpStatus.NOT_FOUND, "URL mapping not found", null);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody ErrorResp requestHandlingNoHandlerFound(Exception ex, WebRequest request) {
		log.error("Exception while processing: ", ex);
		return new ErrorResp(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error",
				Collections.singletonList(ex.getMessage()));
	}

}
