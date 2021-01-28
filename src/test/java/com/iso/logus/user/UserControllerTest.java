package com.iso.logus.user;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iso.logus.domain.user.controller.UserController;
import com.iso.logus.domain.user.domain.User;
import com.iso.logus.domain.user.domain.UserRepository;
import com.iso.logus.domain.user.service.UserService;

@ActiveProfiles("test")
public class UserControllerTest {

	@InjectMocks
	private UserController userController;
	
	@Mock
	private UserService userService;
	
	@Mock
	private UserRepository userRepository;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	private MockMvc mockMvc;
	
	private User user;
}
