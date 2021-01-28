package com.iso.logus.global.error;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponse {
	private String code;
	private String message;
	private List<FieldError> errors = new ArrayList<>();
	
	@Builder
	public ErrorResponse(String code, String message, List<FieldError> errors) {
		this.code = code;
		this.message = message;
		this.errors = initErrors(errors);
	}
	
	private List<FieldError> initErrors(List<FieldError> errors){
		return (errors == null) ? new ArrayList<>() : errors;
	}
	
	@Getter
	public static class FieldError {
		private String field;
		private String value;
		private String reason;
		
		@Builder
		public FieldError(String field, String value, String reason) {
			this.field = field;
			this.value = value;
			this.reason = reason;
		}
	}
}
