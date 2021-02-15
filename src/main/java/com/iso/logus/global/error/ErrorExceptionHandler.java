package com.iso.logus.global.error;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.iso.logus.domain.team.exception.TeamAuthNameDuplicationException;
import com.iso.logus.domain.team.exception.TeamNotFoundException;
import com.iso.logus.domain.user.exception.PasswordFailedExceededException;
import com.iso.logus.domain.user.exception.UidDuplicationException;
import com.iso.logus.domain.user.exception.UserNotFoundException;
import com.iso.logus.domain.user.exception.WrongPasswordException;
import com.iso.logus.global.exception.AccessDeniedException;
import com.iso.logus.global.exception.CustomException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@ResponseBody
@Slf4j
public class ErrorExceptionHandler {
	
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ErrorResponse handleMethodException(MethodArgumentNotValidException e) {
        final List<ErrorResponse.FieldError> fieldErrors = getFieldErrors(e.getBindingResult());
		return build(ErrorCode.REQUEST_ERROR, e, fieldErrors);
	}
	
	@ExceptionHandler(value = {WrongPasswordException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST) //400_ERROR
	protected ErrorResponse handleBadRequestException(CustomException e) {
		return build(e.getErrorCode(), e);
	}
	
	@ExceptionHandler(value = {AccessDeniedException.class})
	@ResponseStatus(HttpStatus.UNAUTHORIZED) //401_ERROR
	protected ErrorResponse handleUnauthorizedException(CustomException e) {
		return build(e.getErrorCode(), e);
	}
	
	@ExceptionHandler(value = {UserNotFoundException.class, TeamNotFoundException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND) //404_ERROR
	protected ErrorResponse handleNotFoundException(CustomException e) {
		return build(e.getErrorCode(), e);
	}
	
	@ExceptionHandler(value = {UidDuplicationException.class, TeamAuthNameDuplicationException.class})
	@ResponseStatus(HttpStatus.CONFLICT) //409_ERROR
	protected ErrorResponse handleConflictException(CustomException e) {
		return build(e.getErrorCode(), e);
	}
	
	@ExceptionHandler(value = {PasswordFailedExceededException.class})
	@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS) //429_ERROR
	protected ErrorResponse handleTooManyRequestsException(CustomException e) {
		return build(e.getErrorCode(), e);
	}
	
	
	private List<ErrorResponse.FieldError> getFieldErrors(BindingResult bindingResult) {
		final List<FieldError> errors = bindingResult.getFieldErrors();
		return errors.parallelStream()
				.map(error -> ErrorResponse.FieldError.builder()
						.reason(error.getDefaultMessage())
						.field(error.getField())
						.value((String) error.getRejectedValue())
						.build())
				.collect(Collectors.toList());
	}
	
	private ErrorResponse build(ErrorCode errorCode, Exception e) {
		buildLog(errorCode, e);
		return buildError(errorCode);
	}
	
	private ErrorResponse build(ErrorCode errorCode, Exception e, List<ErrorResponse.FieldError> errors) {
		buildLog(errorCode, e);
		return buildError(errorCode, errors);
	}
	
	private void buildLog(ErrorCode errorCode, Exception e) {
		log.error(errorCode.getMessage(), e.getMessage());
	}
	
	private ErrorResponse buildError(ErrorCode errorCode) {
		return ErrorResponse.builder()
					.code(errorCode.getCode())
					.message(errorCode.getMessage())
					.build();
	}
	
	private ErrorResponse buildError(ErrorCode errorCode, List<ErrorResponse.FieldError> errors) {
		return ErrorResponse.builder()
					.code(errorCode.getCode())
					.message(errorCode.getMessage())
					.errors(errors)
					.build();
	}
}
