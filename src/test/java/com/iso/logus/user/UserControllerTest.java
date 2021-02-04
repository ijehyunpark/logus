package com.iso.logus.user;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iso.logus.ApiDocumentUtils;
import com.iso.logus.domain.user.domain.Password;
import com.iso.logus.domain.user.domain.User;
import com.iso.logus.domain.user.dto.UserDto;
import com.iso.logus.domain.user.service.UserService;
import com.iso.logus.global.jwt.JwtTokenProvider;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureRestDocs
@AutoConfigureMockMvc
//@WebMvcTest(UserController.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserControllerTest {
	
	@Autowired
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@MockBean
	private UserService userService;
	
	private User user;
	
	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
						.apply(documentationConfiguration(restDocumentationContextProvider))
						.addFilters(new CharacterEncodingFilter("UTF-8", true))
						.alwaysDo(print())
						.build();
		
		user = User.builder()
					.uid("testUser")
					.name("test-user")
					.password(Password.builder().value("password").build())
					.build();
	}
	
	@Test
	public void findUserByUidTest() throws Exception {
		//given
		given(userService.findByUid("testUser")).willReturn(new UserDto.Response(user));
		
		//when
		ResultActions result = mockMvc.perform(get("/api/user/testUser")
										.accept(MediaType.APPLICATION_JSON));
		
		//then
		result.andExpect(status().isOk())
				.andDo(document("user-findByUid",
						ApiDocumentUtils.getDocumentRequest(),
						ApiDocumentUtils.getDocumentResponse(),
						responseFields(
							fieldWithPath("uid").description("유저 아이디"),
							fieldWithPath("name").description("유저 이름"),
							fieldWithPath("createdDate").description("계정 생성 일자"),
							fieldWithPath("lastModifiedDate").description("계정 정보 수정 일자")
							)
						));
	}
	
	@Test
	public void SignUpTest() throws Exception {
		//given
		UserDto.SignUpRequest dto = UserDto.SignUpRequest.builder()
															.uid("testUser")
															.name("test-user")
															.password("password")
															.build();
		
		//when	
		ResultActions result = mockMvc.perform(post("/api/user/join")
					.content(objectMapper.writeValueAsString(dto))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
				);
		
		//then
		result.andExpect(status().isCreated())
				.andDo(document("user-signUp",
						ApiDocumentUtils.getDocumentRequest(),
						ApiDocumentUtils.getDocumentResponse(),
						requestFields(
							fieldWithPath("uid").type(JsonFieldType.STRING).description("유저 아이디"),
							fieldWithPath("name").type(JsonFieldType.STRING).description("유저 이름"),
							fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
							)
						)); 
	}
	
	@Test
	public void changeUserNameTest() throws Exception {
		//given
		UserDto.ChangeNameRequest dto = UserDto.ChangeNameRequest.builder()
																	.name("changedName")
																	.build();
		String token = jwtTokenProvider.createToken("testUser");
		
		//when
		ResultActions result = mockMvc.perform(patch("/api/user/testUser")
					.content(objectMapper.writeValueAsString(dto))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.header("X-AUTH-TOKEN", token)
				);
		
		//then
		result.andExpect(status().isOk())
				.andDo(document("user-changeUserName",
						ApiDocumentUtils.getDocumentRequest(),
						ApiDocumentUtils.getDocumentResponse(),
						requestFields(
							fieldWithPath("name").type(JsonFieldType.STRING).description("유저 이름")
							)
						)); 
		
	}
	
	@Test
	public void deleteuserByUidTest() throws Exception {
		//given
		String token = jwtTokenProvider.createToken("testUser");
				
		//when
		ResultActions result = mockMvc.perform(delete("/api/user/testUser")
												.header("X-AUTH-TOKEN", token));
				
		//then
		result.andExpect(status().isOk())
				.andDo(document("user-deleteUserByUid",
						ApiDocumentUtils.getDocumentRequest(),
						ApiDocumentUtils.getDocumentResponse()
						));
	}
	
	@Test
	public void signInTest() throws Exception {
		//given
		UserDto.SignInRequest dto = UserDto.SignInRequest.builder()
															.uid("testUser")
															.password("password")
															.build();
		
		//when
		ResultActions result = mockMvc.perform(post("/api/user/login")
					.content(objectMapper.writeValueAsString(dto))
					.contentType(MediaType.APPLICATION_JSON));
				
		//then
		result.andExpect(status().isOk())
				.andDo(document("user-signIn",
						ApiDocumentUtils.getDocumentRequest(),
						ApiDocumentUtils.getDocumentResponse()
						));
	}
	
	@Test
	public void findUserByUserNameTest() throws Exception {
		//given
		List<UserDto.Response> dtoList = new ArrayList<>();
		dtoList.add(new UserDto.Response(user));
		given(userService.findByUserName("testUser")).willReturn(dtoList);
		
		//when
		ResultActions result = mockMvc.perform(get("/api/user/find/testUser")
										.accept(MediaType.APPLICATION_JSON));
		
		//then
		result.andExpect(status().isOk())
				.andDo(document("user-findByUserName",
						ApiDocumentUtils.getDocumentRequest(),
						ApiDocumentUtils.getDocumentResponse(),
						responseFields(
							fieldWithPath("[].uid").description("유저 아이디"),
							fieldWithPath("[].name").description("유저 이름"),
							fieldWithPath("[].createdDate").description("계정 생성 일자"),
							fieldWithPath("[].lastModifiedDate").description("계정 정보 수정 일자")
							)
						));
	}
}
