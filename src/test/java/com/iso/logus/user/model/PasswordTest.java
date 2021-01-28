package com.iso.logus.user.model;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import com.iso.logus.domain.user.domain.Password;
import com.iso.logus.domain.user.exception.PasswordFailedExceededException;

@ActiveProfiles("test")
public class PasswordTest {
	private static final String rawPassword = "impassword";
	private Password password;
	
	@BeforeEach
	public void setUp() {
		password = Password.builder()
							.value(rawPassword)
							.build();
	}
	
	@Test
	public void passwordTest() {
		assertThat(password.isMatched(rawPassword), is(true));
		assertThat(password.getFailedCount(), is(0));
		assertThat(password.getValue(), is(notNullValue()));
	}
	
	@Test
	@DisplayName("실패 횟수 초기화 테스트")
	public void resetFailedCountTest() {
		password.isMatched("wrongpassword");
		password.isMatched("wrongpassword");
		password.isMatched("wrongpassword");
		
		assertThat(password.getFailedCount(), is(3));
		
		password.isMatched(rawPassword);
		
		assertThat(password.getFailedCount(), is(0));
	}
	
	@Test
	@DisplayName("실패 횟수 1씩 증가 & 정상 초기화 테스트")
	public void normalFailedCountIncreaseTest() {
		assertThat(password.getFailedCount(), is(0));
		
		password.isMatched("wrongpassword");
		assertThat(password.getFailedCount(), is(1));
		
		password.isMatched("wrongpassword");
		assertThat(password.getFailedCount(), is(2));
		
		password.isMatched("wrongpassword");
		assertThat(password.getFailedCount(), is(3));
		
		password.isMatched("wrongpassword");
		assertThat(password.getFailedCount(), is(4));
		
		password.isMatched(rawPassword);
		assertThat(password.getFailedCount(), is(0));
	}	
	
	@Test
	@DisplayName("계정 잠금 테스트")
	public void passwordFailedExceededExceptionTest() {
		password.isMatched("wrongpassword");
		password.isMatched("wrongpassword");
		password.isMatched("wrongpassword");
		password.isMatched("wrongpassword");
		password.isMatched("wrongpassword");
		assertThrows(PasswordFailedExceededException.class, () -> {
			password.isMatched("wrongpassword");
		});
	}
	
	@Test
	@DisplayName("비밀번호 변경 테스트")
	public void changePasswordTest() {
		password.changePassword("newpassword", rawPassword);
		assertThat(password.isMatched("newpassword"), is(true));
		assertThat(password.getFailedCount(), is(0));
		assertThat(password.isMatched(rawPassword), is(false));
		assertThat(password.getFailedCount(), is(1));
	}
	
	@Test
	@DisplayName("비밀번호 변경 실패")
	public void changePasswordFailTest() {
		password.changePassword("newpassword", "wrong password");
		assertThat(password.getFailedCount(), is(1));
		
		password.changePassword("newpassword", "wrong password");
		assertThat(password.getFailedCount(), is(2));
		
		
		assertThat(password.isMatched(rawPassword), is(true));
		assertThat(password.isMatched("wrong password"), is(false));
	}
	
	@Test
	@DisplayName("비밀번호 변경 지속적으로 실패시 계정 잠금 테스트")
	public void changePasswordFailedExceededExceptionTest() {
		password.changePassword("newpassword", "wrong password");
		password.changePassword("newpassword", "wrong password");
		password.changePassword("newpassword", "wrong password");
		password.changePassword("newpassword", "wrong password");
		password.changePassword("newpassword", "wrong password");
		assertThrows(PasswordFailedExceededException.class, () ->{
		password.changePassword("newpassword", "wrong password");
		});
	}
}
