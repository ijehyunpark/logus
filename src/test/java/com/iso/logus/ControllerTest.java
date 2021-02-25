package com.iso.logus;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iso.logus.global.jwt.JwtTokenProvider;


@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Disabled
public class ControllerTest {
	
	@Autowired 
	protected ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired 
	protected MockMvc mockMvc;
	
	@Autowired
	protected JwtTokenProvider jwtTokenProvider;
	
	protected void setUpMockMvc(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider) {
		this.mockMvc = MockMvcBuilders
				.webAppContextSetup(webApplicationContext)
				.apply(documentationConfiguration(restDocumentationContextProvider))
				.addFilters(new CharacterEncodingFilter("UTF-8", true))
				.alwaysDo(print())
				.build();
	}
	
	protected ResultActions performGet(String url) throws Exception {
		return mockMvc.perform(get(url)
						.accept(MediaType.APPLICATION_JSON));
	}
	
	protected ResultActions performGet(String url, String token) throws Exception {	
		return mockMvc.perform(get(url)
						.accept(MediaType.APPLICATION_JSON)
						.header("X-AUTH-TOKEN", token));
	}
	
	protected ResultActions performGet(String url, MultiValueMap<String, String> params) throws Exception {
		return mockMvc.perform(get(url)
						.accept(MediaType.APPLICATION_JSON)
						.params(params));
		
	}
	
	protected ResultActions performGet(String url, MultiValueMap<String, String> params, String token) throws Exception {
		return mockMvc.perform(get(url)
						.accept(MediaType.APPLICATION_JSON)
						.params(params)
						.header("X-AUTH-TOKEN", token));
	}
	
	protected ResultActions performPost(String url, Object dto) throws Exception {
		return mockMvc.perform(post(url)
						.content(objectMapper.writeValueAsString(dto))
						.contentType(MediaType.APPLICATION_JSON));

	}
	
	protected ResultActions performPost(String url, Object dto, String token) throws Exception {
		return mockMvc.perform(post(url)
						.content(objectMapper.writeValueAsString(dto))
						.contentType(MediaType.APPLICATION_JSON)
						.header("X-AUTH-TOKEN", token));

	}
	
	protected ResultActions performPut(String url, Object dto) throws Exception {
		return mockMvc.perform(put(url)
				.content(objectMapper.writeValueAsString(dto))
				.contentType(MediaType.APPLICATION_JSON));
	}
	
	protected ResultActions performPut(String url, Object dto, String token) throws Exception {
		return mockMvc.perform(put(url)
				.content(objectMapper.writeValueAsString(dto))
				.contentType(MediaType.APPLICATION_JSON)
				.header("X-AUTH-TOKEN", token));
	}
	
	protected ResultActions performPatch(String url, Object dto) throws Exception {
		return mockMvc.perform(patch(url)
				.content(objectMapper.writeValueAsString(dto))
				.contentType(MediaType.APPLICATION_JSON));
	}
	
	protected ResultActions performPatch(String url, Object dto, String token) throws Exception {
		return mockMvc.perform(patch(url)
				.content(objectMapper.writeValueAsString(dto))
				.contentType(MediaType.APPLICATION_JSON)
				.header("X-AUTH-TOKEN", token));
	}
		
	protected ResultActions performDelete(String url) throws Exception {
		return mockMvc.perform(delete(url));
	}
	
	protected ResultActions performDelete(String url, String token) throws Exception {
		return mockMvc.perform(delete(url)
						.header("X-AUTH-TOKEN", token));
	}
}
