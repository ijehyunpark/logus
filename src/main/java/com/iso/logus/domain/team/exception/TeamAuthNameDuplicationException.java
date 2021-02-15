package com.iso.logus.domain.team.exception;

import com.iso.logus.global.error.ErrorCode;
import com.iso.logus.global.exception.CustomException;

import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Getter
@NoArgsConstructor
public class TeamAuthNameDuplicationException extends CustomException{
	
	private ErrorCode errorCode = ErrorCode.TEAM_AUTH_DUPLICATION;
}
