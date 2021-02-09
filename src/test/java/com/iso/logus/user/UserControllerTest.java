package com.iso.logus.user;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import com.iso.logus.ApiDocumentUtils;
import com.iso.logus.ControllerTest;
import com.iso.logus.domain.user.domain.Password;
import com.iso.logus.domain.user.domain.User;
import com.iso.logus.domain.user.dto.UserDto;
import com.iso.logus.domain.user.service.UserService;

public class UserControllerTest extends ControllerTest {

	@MockBean
	private UserService userService;
	
	private int USER_NUM = 0;
	
	private User user;
	
	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider) {
		setUpMockMvc(webApplicationContext, restDocumentationContextProvider);
		
		user = signUpRequestBuilder().toEntity();
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
		UserDto.SignUpRequest dto = signUpRequestBuilder();
		
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
	@DisplayName("유저 이름 변경 시도_본인이 아닌 경우")
	public void changeUserNameTest_accessDenied() throws Exception {
		//given
		UserDto.ChangeNameRequest dto = UserDto.ChangeNameRequest.builder()
																	.name("changedName")
																	.build();
		String token = jwtTokenProvider.createToken("idk");
		//when
		ResultActions result = mockMvc.perform(patch("/api/user/testUser")
				.content(objectMapper.writeValueAsString(dto))
				.contentType(MediaType.APPLICATION_JSON)
				.header("X-AUTH-TOKEN", token)
				);
		
		//then
		result.andExpect(status().isUnauthorized())
			.andDo(document("user-changeUserName-accessDenied",
			ApiDocumentUtils.getDocumentRequest(),
			ApiDocumentUtils.getDocumentResponse()
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
	@DisplayName("유저 삭제 시도_본인이 아닌 경우")
	public void deleteuserByUidTest_accessDenied() throws Exception {
		//given
		String token = jwtTokenProvider.createToken("idk");
				
		//when
		ResultActions result = mockMvc.perform(delete("/api/user/testUser")
												.header("X-AUTH-TOKEN", token));
				
		//then
		result.andExpect(status().isUnauthorized())
				.andDo(document("user-deleteUserByUid-accessDenied",
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
	
	private UserDto.SignUpRequest signUpRequestBuilder() {
		USER_NUM++;
		return UserDto.SignUpRequest.builder()
				.uid("testUser" + String.valueOf(USER_NUM))
				.name("test-user" + String.valueOf(USER_NUM))
				.password("password")
				.build();
	}
}
