package com.iso.logus.domain.user.exception;

import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class UserNotFoundException extends RuntimeException {

	private String uid;
	
	public UserNotFoundException(String uid) {
		this.uid = uid;
	}
}
