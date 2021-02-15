package com.iso.logus.user;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.iso.logus.domain.user.domain.Password;
import com.iso.logus.domain.user.domain.User;
import com.iso.logus.domain.user.domain.UserRepository;
import com.iso.logus.domain.user.dto.UserDto;
import com.iso.logus.domain.user.exception.UserNotFoundException;
import com.iso.logus.domain.user.exception.WrongPasswordException;
import com.iso.logus.domain.user.service.UserService;
import com.iso.logus.global.jwt.JwtTokenProvider;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	
	@InjectMocks
	private UserService userService;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private JwtTokenProvider jwtTokenProvider;
	
	private User user;
	
	@BeforeEach
	public void setUp() {
		user = User.builder()
					.uid("testUser")
					.password(Password.builder().value("password").build())
					.name("test-user")
					.build();
	}
	
	@Test
	@DisplayName("findByUid: 해당 uid를 가진 회원 리턴_성공")
	public void findByUidTest_success() {
		//given
		given(userRepository.findByUid("testUser")).willReturn(Optional.of(user));
		
		//when
		final User findUser = userService.findUserByUid("testUser");
		
		//then
		assertNotNull(findUser);
		assertThat(findUser.getName(), is("test-user"));
	}
	
	@Test
	@DisplayName("findByUid: 해당 uid를 가진 회원 리턴_실패")
	public void findByUidTest_fail() {
		//given
		given(userRepository.findByUid(anyString())).willReturn(Optional.empty());
		
		//when
		assertThrows(UserNotFoundException.class, () -> {
			userService.findByUid("testUser");
		});
		//then
		
	}
	
	@Test
	@DisplayName("isExistedUid: 해당 uid를 가진 회원이 존재하는 경우_true")
	public void isExistedUidTest_true() {
		//given
		given(userRepository.existsByUid("testUser")).willReturn(true);
		
		//when
		final boolean existedUid = userService.isExistedUid("testUser");
		
		//then
		assertTrue(existedUid);
	}
	
	@Test
	@DisplayName("isExistedUid: 해당 uid를 가진 회원이 존재하지 않는 경우_false")
	public void isExistedUidTest_false() {
		//given
		given(userRepository.existsByUid("testUser")).willReturn(false);
		
		//when
		final boolean existedUid = userService.isExistedUid("testUser");
		
		//then
		assertFalse(existedUid);
	}
	
	@Test
	@DisplayName("changeUserName: 유저 이름 변경 테스트_성공")
	public void changeUserNameTest_success() {
		//given
		given(userRepository.findByUid("testUser")).willReturn(Optional.of(user));
		UserDto.ChangeNameRequest changeNameRequest = UserDto.ChangeNameRequest.builder()
									.name("changeUser")
									.build();
		
		//when
		userService.changeUserName("testUser", changeNameRequest);
		
		//then
		assertEquals(user.getName(), "changeUser");
	}
	
	@Test
	@DisplayName("changeUserName: 유저 이름 변경 테스트_실패")
	public void changeUserNameTest_false() {
		//given
		given(userRepository.findByUid("testUser")).willReturn(Optional.empty());
		UserDto.ChangeNameRequest changeNameRequest = UserDto.ChangeNameRequest.builder()
									.build();
		
		//when
		assertThrows(UserNotFoundException.class, () -> {
			userService.changeUserName("testUser", changeNameRequest);
		});
		
		//then
	}
	
	@Test
	@DisplayName("deleteUserByUid: 유저 삭제 테스트_성공")
	public void deleteUserByUidTest_success() {
		//given
		given(userRepository.findByUid("testUser")).willReturn(Optional.of(user));
		
		//when
		userService.deleteUserByUid("testUser");
		
		//then
		verify(userRepository, times(1)).delete(user);
	}
	
	@Test
	@DisplayName("deleteUserByUid: 유저 삭제 테스트_실패")
	public void deleteUserByUidTest_fail() {
		//given
		given(userRepository.findByUid("testUser")).willReturn(Optional.empty());
		
		//when
		assertThrows(UserNotFoundException.class, () -> {
			userService.deleteUserByUid("testUser");
		});
		
		//then
	}
	
	@Test
	@DisplayName("signUp: 새로운 계정 생성 테스트")
	public void singUpTest() {
		//given
		UserDto.SignUpRequest signUpRequest = UserDto.SignUpRequest.builder()
				.uid("newAccount")
				.password("password")
				.name("testAccount")
				.build();
		
		//when
		userService.SignUp(signUpRequest);
		
		//then
		
	}
	
	@Test
	@DisplayName("signIn: 로그인 테스트_성공")
	public void signInTest_success() {
		//given
		given(userRepository.findByUid("testUser")).willReturn(Optional.of(user));
		given(jwtTokenProvider.createToken("testUser")).willReturn("DxdDIsovusi...");
		UserDto.SignInRequest signInRequest = UserDto.SignInRequest.builder()
																	.uid("testUser")
																	.password("password")
																	.build();
		
		//when
		String resultToken = userService.signIn(signInRequest);
		
		//then
		assertNotNull(resultToken);
		assertNotSame("", resultToken);
	}
	
	@Test
	@DisplayName("signIn: 로그인 테스트_실패: 잘못된 비밀번호")
	public void signInTest_fail1() {
		//given
		given(userRepository.findByUid("testUser")).willReturn(Optional.of(user));
		UserDto.SignInRequest signInRequest = UserDto.SignInRequest.builder()
																	.uid("testUser")
																	.password("wrong")
																	.build();
		
		//when
		assertThrows(WrongPasswordException.class, () -> {
			String resultToken = userService.signIn(signInRequest);
		});
		
		//then
	}
	
	@Test
	@DisplayName("signIn: 로그인 테스트_실패:없는 계정, 특이사항: 잘못된 비밀번호 예외를 발생시켜야 함")
	public void signInTest_fail2() {
		//given
		given(userRepository.findByUid("empty")).willReturn(Optional.empty());
		UserDto.SignInRequest signInRequest = UserDto.SignInRequest.builder()
																	.uid("empty")
																	.password("password")
																	.build();
		
		//when
		assertThrows(WrongPasswordException.class, () -> {
			String resultToken = userService.signIn(signInRequest);
		});
		
		//then
	}
	
	@Test
	@DisplayName("findByUserName: 이름으로 유저 찾기")
	public void findByUserNameTest() {
		//given
		User sample1 = User.builder()
					.uid("39483")
					.password(Password.builder().value("password").build())
					.name("test-user")
					.build();
		given(userRepository.findByName("test-user")).willReturn(List.of(user, sample1));
		
		//when
		List<UserDto.Response> dto = userService.findByUserName("test-user");
		
		//then
		assertNotNull(dto);
	}
}
