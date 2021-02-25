package com.iso.logus.global.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import lombok.Setter;

@Setter
public class CustomPageRequest {
	@PositiveOrZero
	private int page = 0;
	@PositiveOrZero @Max(100)
	private int size = 20;
	private Sort.Direction direction = Sort.Direction.ASC;
	
	public PageRequest of() {
		return PageRequest.of(page, size, direction, "createdDate");
	}
}
