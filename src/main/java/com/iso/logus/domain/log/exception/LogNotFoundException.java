package com.iso.logus.domain.log.exception;

import com.iso.logus.global.error.ErrorCode;
import com.iso.logus.global.exception.CustomException;

import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Getter
@NoArgsConstructor
public class LogNotFoundException extends CustomException {

	private ErrorCode errorCode = ErrorCode.LOG_NOT_FOUND;
}
