package com.iso.logus.domain.user.dto;

import java.time.LocalDateTime;

import javax.validation.Valid;
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
	public static class SingUpRequest {
		@NotEmpty
		private String uid;
		@NotEmpty
		private String name;
		
		private String password;
		
		@Builder
		public SingUpRequest(String uid, String name, String password) {
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
		private String name;
		
		@Builder
		public ChangeNameRequest(String name) {
			this.name = name;
		}
		
		
	}
}
