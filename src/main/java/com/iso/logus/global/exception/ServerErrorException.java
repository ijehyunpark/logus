package com.iso.logus.global.exception;

import com.iso.logus.global.error.ErrorCode;

import lombok.NoArgsConstructor;

import lombok.Getter;

@SuppressWarnings("serial")
@Getter
@NoArgsConstructor
public class ServerErrorException extends CustomException {
	
	private ErrorCode errorCode = ErrorCode.SERVER_ERROR;
}
