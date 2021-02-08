package com.iso.logus.global.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.iso.logus.domain.team.exception.TeamNotFoundException;
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
		return build(ErrorCode.ACCESS_DENIED, e);
	}
	
	@ExceptionHandler(value = {UserNotFoundException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	protected ErrorResponse handleUserNotFoundException(UserNotFoundException e) {
		return build(ErrorCode.USER_NOT_FOUND, e);
	}
	
	@ExceptionHandler(value = {UidDuplicationException.class})
	@ResponseStatus(HttpStatus.CONFLICT)
	protected ErrorResponse handleUidDuplicationException(UidDuplicationException e) {
		return build(ErrorCode.UID_DUPLICATION, e);
	}
	
	@ExceptionHandler(value = {WrongPasswordException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ErrorResponse handleWrongPasswordException(WrongPasswordException e) {
		return build(ErrorCode.WRONG_PASSWORD, e);
	}
	
	@ExceptionHandler(value = {PasswordFailedExceededException.class})
	@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
	protected ErrorResponse handlePasswordFailedExceededException(PasswordFailedExceededException e) {
		return build(ErrorCode.PASSWORD_FAILED_EXCEEDED, e);
	}
	
	@ExceptionHandler(value = {TeamNotFoundException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	protected ErrorResponse handleTeamNotFoundException(PasswordFailedExceededException e) {
		return build(ErrorCode.TEAM_NOT_FOUND, e);
	}
	
	private ErrorResponse build(ErrorCode errorCode, RuntimeException e) {
		buildLog(errorCode, e);
		return buildError(errorCode);
	}
	
	private void buildLog(ErrorCode errorCode, RuntimeException e) {
		log.error(errorCode.getMessage(), e.getMessage());
	}
	
	private ErrorResponse buildError(ErrorCode errorCode) {
		return ErrorResponse.builder()
					.code(errorCode.getCode())
					.message(errorCode.getMessage())
					.build();
	}
}
