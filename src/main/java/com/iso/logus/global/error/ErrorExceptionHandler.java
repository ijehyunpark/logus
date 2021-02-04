package com.iso.logus.global.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.iso.logus.domain.user.exception.PasswordFailedExceededException;
import com.iso.logus.domain.user.exception.UidDuplicationException;
import com.iso.logus.domain.user.exception.UserNotFoundException;
import com.iso.logus.domain.user.exception.WrongPasswordException;
import com.iso.logus.global.exception.AccessDeniedException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@ResponseBody
@Slf4j
public class ErrorExceptionHandler {
	@ExceptionHandler(value = {AccessDeniedException.class})
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	protected ErrorResponse handleAccessDeniedException(AccessDeniedException e) {
		final ErrorCode accessDeniedException = ErrorCode.ACCESS_DENIED;
		log.error(accessDeniedException.getMessage(), e.getMessage());
		return buildError(accessDeniedException);
	}
	
	@ExceptionHandler(value = {UserNotFoundException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	protected ErrorResponse handleUserNotFoundException(UserNotFoundException e) {
		final ErrorCode userNotFound = ErrorCode.USER_NOT_FOUND;
		log.error(userNotFound.getMessage(), e.getMessage());
		return buildError(userNotFound);
	}
	
	@ExceptionHandler(value = {UidDuplicationException.class})
	@ResponseStatus(HttpStatus.CONFLICT)
	protected ErrorResponse handleUidDuplicationException(UidDuplicationException e) {
		final ErrorCode uidDuplication = ErrorCode.UID_DUPLICATION;
		log.error(uidDuplication.getMessage(), e.getMessage());
		return buildError(uidDuplication);
	}
	
	@ExceptionHandler(value = {WrongPasswordException.class})
	@ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
	protected ErrorResponse handleWrongPasswordException(WrongPasswordException e) {
		final ErrorCode wrongPassword = ErrorCode.WRONG_PASSWORD;
		log.error(wrongPassword.getMessage(), e.getMessage());
		return buildError(wrongPassword	);
	}
	
	@ExceptionHandler(value = {PasswordFailedExceededException.class})
	@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
	protected ErrorResponse handlePasswordFailedExceededException(PasswordFailedExceededException e) {
		final ErrorCode passwordFailedExceeded = ErrorCode.PASSWORD_FAILED_EXCEEDED;
		log.error(passwordFailedExceeded.getMessage(), e.getMessage());
		return buildError(passwordFailedExceeded);
	}
	
	private ErrorResponse buildError(ErrorCode errorCode) {
		return ErrorResponse.builder()
					.code(errorCode.getCode())
					.message(errorCode.getMessage())
					.build();
	}
}
