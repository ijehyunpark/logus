package com.iso.logus.user;

import com.iso.logus.domain.user.domain.User;
import com.iso.logus.domain.user.dto.UserDto;

public class UserTestSampleData {
	public UserDto.SignUpRequest createSignUpRequest() {
		return UserDto.SignUpRequest.builder()
						.uid("user")
						.name("user")
						.password("password")
						.build();
	}
	
	public UserDto.SignUpRequest createSignUpRequest(long contentPivot) {
		return UserDto.SignUpRequest.builder()
						.uid("user" + contentPivot)
						.name("user" + contentPivot)
						.password("password")
						.build();
	}
	
	public User buildUser() {
		return createSignUpRequest().toEntity();
	}
	
	public User buildUser(long contentPivot) {
		return createSignUpRequest(contentPivot).toEntity();
	}
}
