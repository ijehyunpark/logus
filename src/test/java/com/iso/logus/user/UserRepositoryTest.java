package com.iso.logus.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.iso.logus.domain.user.domain.User;
import com.iso.logus.domain.user.domain.UserRepository;
import com.iso.logus.domain.user.dto.UserDto;
import com.iso.logus.domain.user.exception.UserNotFoundException;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {
	@Autowired
	private UserRepository userRepository;
	
	private static final String uid = "TestUser";
	
	@BeforeEach
	public void setUp() {
		User user = UserDto.SignUpRequest.builder()
			.uid(uid)
			.password("hellotest")
			.name("testuser")
			.build()
			.toEntity();
		userRepository.save(user);
	}
	
	@Test
	public void findByUid() {
		final User user = userRepository.findByUid(uid).orElseThrow(UserNotFoundException::new);
		assertThat(user.getUid()).isEqualTo(uid);
	}
}
