package com.iso.logus.global.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.iso.logus.domain.user.exception.PasswordFailedExceededException;
import com.iso.logus.domain.user.exception.UidDuplicationException;
import com.iso.logus.domain.user.exception.UserNotFoundException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@ResponseBody
@Slf4j
public class ErrorExceptionController {
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
