package com.iso.logus.domain.log.domain;

import lombok.Getter;

@Getter
public enum KeywordType {
	titleonly(0),
	contentonly(1),
	titleandcontent(2),
	author(3),
	date(4);
	
	private final long keywordId;
	
	private KeywordType(long keywordId) {
		this.keywordId = keywordId;
	}
}
