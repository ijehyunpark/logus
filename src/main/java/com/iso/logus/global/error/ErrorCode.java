package com.iso.logus.global.error;

import lombok.Getter;

@Getter
public enum ErrorCode {
	/* Global Exception : -0xx */
	SERVER_ERROR("-1", "알 수 없는 에러가 발생하였습니다."),
	ACCESS_DENIED("-2", "권한이 없습니다"),
	REQUEST_ERROR("-3", "올바르지 않은 요청입니다."),
	
	/* User Exception : -1xx */
	USER_NOT_FOUND("-100", "해당 유저를 찾을 수 없습니다."),
	UID_DUPLICATION("-101", "중복된 아이디를 가진 유저가 존재합니다."),
	WRONG_PASSWORD("-102", "아이디 혹은 비밀번호가 잘못되었습니다."),
	PASSWORD_FAILED_EXCEEDED("-103","비밀번호 실패 횟수가 초과했습니다."),
	
	/* Team Exception : -2xx */
	TEAM_NOT_FOUND("-200", "해당 팀을 찾을 수 없습니다."),
	TEAM_AUTH_NOT_FOUND("-201", "해당 권한을 찾을 수 없습니다."),
	TEAM_AUTH_DUPLICATION("-202", "중복된 이름을 가진 팀 권한이 존재합니다.");
	
	private String code;
	private String message;
	
	ErrorCode(String code, String message) {
		this.code = code;
		this.message = message;
	}
}
