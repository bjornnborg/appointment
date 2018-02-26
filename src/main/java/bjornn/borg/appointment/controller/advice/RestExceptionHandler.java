package bjornn.borg.appointment.controller.advice;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import bjornn.borg.appointment.exception.MandatoryDataMissingException;
import bjornn.borg.appointment.exception.UnavailableTimeSlotException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler({UnavailableTimeSlotException.class, MandatoryDataMissingException.class})
	protected ResponseEntity<Object> handle(RuntimeException rte, WebRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-type", "application/json");
		String message = String.format("{\"error\": \"%s\"}", rte.getMessage());
		HttpStatus status = rte.getClass().isAssignableFrom(UnavailableTimeSlotException.class) ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
		
		return this.handleExceptionInternal(rte, message, headers, status, request);
	}
	
	
}
