package com.iso.logus.domain.team.exception;

import com.iso.logus.global.error.ErrorCode;
import com.iso.logus.global.exception.CustomException;

import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Getter
@NoArgsConstructor
public class TeamAuthMasterAuthException extends CustomException {
	private ErrorCode errorCode = ErrorCode.MASTER_AUTH_ERROR;
}
