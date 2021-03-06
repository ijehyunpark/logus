package com.iso.logus.domain.user.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;

import com.iso.logus.domain.user.domain.Password;
import com.iso.logus.domain.user.domain.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDto {
	
	@Getter
	public static class Response {
		private String uid;
		private String name;
		private LocalDateTime createdDate;
		private LocalDateTime lastModifiedDate;
		
		
		public Response(User user) {
			this.uid = user.getUid();
			this.name = user.getName();
			this.createdDate = user.getCreatedDate();
			this.lastModifiedDate = user.getLastModifiedDate();
		}
	}
	
	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class SignUpRequest {
		@NotEmpty
		private String uid;
		@NotEmpty
		private String name;
		@NotEmpty
		private String password;
		
		@Builder
		public SignUpRequest(String uid, String name, String password) {
			this.uid = uid;
			this.name = name;
			this.password = password;
		}
		public User toEntity() {
			return User.builder()	
						.uid(this.uid)
						.name(this.name)
						.password(Password.builder().value(this.password).build())
						.build();
		}
	}
	
	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class ChangeNameRequest {
		@NotEmpty
		private String name;
		
		@Builder
		public ChangeNameRequest(String name) {
			this.name = name;
		}
		
	}
	
	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class SignInRequest {
		@NotEmpty
		private String uid;
		@NotEmpty
		private String password;
		
		@Builder
		public SignInRequest(String uid, String password) {
			this.uid = uid;
			this.password = password;
		}
	}
}
