package com.iso.logus.domain.user.exception;

import com.iso.logus.global.error.ErrorCode;
import com.iso.logus.global.exception.CustomException;

import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Getter
@NoArgsConstructor
public class PasswordFailedExceededException extends CustomException {
	
	private ErrorCode errorCode = ErrorCode.PASSWORD_FAILED_EXCEEDED;
	
}
