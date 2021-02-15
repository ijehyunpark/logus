package com.iso.logus.global.exception;

import com.iso.logus.global.error.ErrorCode;

@SuppressWarnings("serial")
public abstract class CustomException extends RuntimeException {

	public ErrorCode errorCode = ErrorCode.SERVER_ERROR;
	
	public abstract ErrorCode getErrorCode();
	
}
