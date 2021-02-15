package com.iso.logus.global.exception;

import com.iso.logus.global.error.ErrorCode;

import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Getter
@NoArgsConstructor
public class AccessDeniedException extends CustomException {

	private ErrorCode errorCode = ErrorCode.ACCESS_DENIED;
}
