package com.olifarhaan.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.olifarhaan.response.ExceptionResponse;

@RestControllerAdvice
public class CustomExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
		logger.error("UsernameNotFoundException: ", ex);
		ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(),
				HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage(), null);
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<ExceptionResponse> handleResponseStatusException(ResponseStatusException ex) {
		logger.error("ResponseStatusException: ", ex);
		ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(), ex.getStatusCode().value(),
				HttpStatus.valueOf(ex.getStatusCode().value()).getReasonPhrase(), ex.getReason(), null);
		return new ResponseEntity<>(response, ex.getStatusCode());
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException ex) {
		logger.error("AccessDeniedException: ", ex);
		ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(), HttpStatus.FORBIDDEN.value(),
				HttpStatus.FORBIDDEN.getReasonPhrase(), "Access denied", null);
		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ExceptionResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
		ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(),
				HttpStatus.NOT_FOUND.getReasonPhrase(), "The requested route is not available", null);
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ExceptionResponse> handleHttpRequestMethodNotSupportedException(
			HttpRequestMethodNotSupportedException ex) {
		ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(),
				HttpStatus.METHOD_NOT_ALLOWED.value(),
				HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(), "The requested route is not available",
				null);
		return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ExceptionResponse> handleMissingServletRequestParameterException(
			MissingServletRequestParameterException ex) {
		ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST.getReasonPhrase(),
				"The request is missing a required parameter: " + ex.getMessage(), null);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MissingServletRequestPartException.class)
	public ResponseEntity<ExceptionResponse> handleMissingServletRequestPartException(
			MissingServletRequestPartException ex) {
		ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST.getReasonPhrase(),
				"The request is missing a required part: " + ex.getMessage(),
				null);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ExceptionResponse> handleMethodArgumentTypeMismatchException(
			MethodArgumentTypeMismatchException ex) {
		ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST.getReasonPhrase(),
				"Invalid request parameter: " + ex.getMessage(), null);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public ResponseEntity<ExceptionResponse> handleDuplicateKeyException(DuplicateKeyException ex) {
		logger.error("DuplicateKeyException: ", ex);
		ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(), HttpStatus.CONFLICT.value(),
				HttpStatus.CONFLICT.getReasonPhrase(), "Duplicate key error: " + ex.getMessage(), null);

		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ExceptionResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
		logger.error("DataIntegrityViolationException: ", ex);
		ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(), HttpStatus.CONFLICT.value(),
				HttpStatus.CONFLICT.getReasonPhrase(), "Data integrity violation: " + ex.getMessage(), null);

		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ExceptionResponse> handleHttpMessageNotReadableException(
			HttpMessageNotReadableException ex) {
		logger.error("HttpMessageNotReadableException: ", ex);
		ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST.getReasonPhrase(), "Invalid request body: " + ex.getMessage(),
				null);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponse> handleValidationException(MethodArgumentNotValidException ex) {
		logger.error("ValidationException: ", ex);
		Map<String, String> validationErrors = new HashMap<>();
		ex.getBindingResult().getFieldErrors()
				.forEach(error -> validationErrors.put(error.getField(), error.getDefaultMessage()));
		ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST.getReasonPhrase(), "Field validation failed", validationErrors);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
		logger.error("IllegalArgumentException: ", ex);
		ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getMessage(), null);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(SecurityException.class)
	public ResponseEntity<ExceptionResponse> handleSecurityException(SecurityException ex) {
		logger.error("SecurityException: ", ex);
		ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(), HttpStatus.UNAUTHORIZED.value(),
				HttpStatus.UNAUTHORIZED.getReasonPhrase(), ex.getMessage(), null);
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler({ CompletionException.class, InterruptedException.class })
	public ResponseEntity<ExceptionResponse> handleConcurrencyException(Exception ex) {
		logger.error("Concurrency Exception: ", ex);
		Throwable cause = ex instanceof CompletionException ? ex.getCause() : ex;

		if (ex instanceof InterruptedException) {
			Thread.currentThread().interrupt();
		}

		ExceptionResponse response = new ExceptionResponse(
				LocalDateTime.now(),
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
				"Error processing concurrent operation: " + cause.getMessage(),
				null);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> handleException(Exception ex) {
		logger.error("Exception: " + ex.getMessage(), ex);
		ex.printStackTrace();
		ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(),
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex.getMessage(), null);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
