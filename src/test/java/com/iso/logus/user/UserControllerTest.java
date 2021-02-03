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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iso.logus.ApiDocumentUtils;
import com.iso.logus.domain.user.controller.UserController;
import com.iso.logus.domain.user.domain.Password;
import com.iso.logus.domain.user.domain.User;
import com.iso.logus.domain.user.dto.UserDto;
import com.iso.logus.domain.user.service.UserService;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureRestDocs
@WebMvcTest(UserController.class)
@ActiveProfiles("test")
public class UserControllerTest {
	
	@Autowired
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private UserService userService;
	
	private User user;
	
	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
						.apply(documentationConfiguration(restDocumentationContextProvider))
						.alwaysDo(print())
						.build();
		
		user = User.builder()
					.uid("testUser")
					.name("test-user")
					.password(Password.builder().value("test1234").build())
					.build();
	}
	
	@Test
	public void findUserByUidTest() throws Exception {
		//given
		given(userService.findByUid("testUser")).willReturn(user);
		
		//when
		ResultActions result = mockMvc.perform(get("/api/user/testUser")
										.accept(MediaType.APPLICATION_JSON));
		
		//then
		result.andExpect(status().isOk())
				.andDo(document("user-findUserByUid",
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
															.password("test1234")
															.build();
		
		//when	
		ResultActions result = mockMvc.perform(post("/api/user")
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
		
		//when
		ResultActions result = mockMvc.perform(patch("/api/user/testUser")
					.content(objectMapper.writeValueAsString(dto))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
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
		
		//when
		ResultActions result = mockMvc.perform(delete("/api/user/testUser"));
				
		//then
		result.andExpect(status().isOk())
				.andDo(document("user-deleteUserByUid",
						ApiDocumentUtils.getDocumentRequest(),
						ApiDocumentUtils.getDocumentResponse()
						));
	}
}
