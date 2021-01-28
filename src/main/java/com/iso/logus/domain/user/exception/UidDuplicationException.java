package com.iso.logus.domain.user.exception;

import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class UidDuplicationException extends RuntimeException {
	
	private String uid;
	
	public UidDuplicationException(String uid) {
		this.uid = uid;
	}
}
