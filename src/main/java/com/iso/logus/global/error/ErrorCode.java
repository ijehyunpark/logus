package com.iso.logus.global.error;

import lombok.Getter;

@Getter
public enum ErrorCode {
	
	/* User Exception : -1xx */
	USER_NOT_FOUND("-100", "해당 유저를 찾을 수 없습니다."),
	UID_DUPLICATION("-101", "중복된 아이디를 가진 유저가 존재합니다."),
	WRONG_PASSWORD("-102", "아이디 혹은 비밀번호가 잘못되었습니다."),
	PASSWORD_FAILED_EXCEEDED("-103","비밀번호 실패 횟수가 초과했습니다.");
	
	private String code;
	private String message;
	
	ErrorCode(String code, String message) {
		this.code = code;
		this.message = message;
	}
}
