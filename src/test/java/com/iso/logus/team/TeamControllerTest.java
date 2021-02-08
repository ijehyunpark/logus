package com.iso.logus.team;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iso.logus.ApiDocumentUtils;
import com.iso.logus.domain.team.domain.Team;
import com.iso.logus.domain.team.dto.TeamDto;
import com.iso.logus.domain.team.service.TeamService;
import com.iso.logus.global.jwt.JwtTokenProvider;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class TeamControllerTest {

	@Autowired
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@MockBean
	private TeamService teamService;
	
	private Team team1, team2;
	
	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
						.apply(documentationConfiguration(restDocumentationContextProvider))
						.addFilters(new CharacterEncodingFilter("UTF-8", true))
						.alwaysDo(print())
						.build();
		team1 = Team.builder()
					.name("testTeam1")
					.descript("sample1")
					.build();
		team2 = Team.builder()
					.name("testTeam2")
					.descript("sample2")
					.build();
	}
	
	@Test
	public void findTeamListTest() throws Exception {
		//given
		List<TeamDto.Response> dtoList = new ArrayList<>();
		dtoList.add(new TeamDto.Response(team1));
		dtoList.add(new TeamDto.Response(team2));
		given(teamService.findList()).willReturn(dtoList);
		
		//when
		ResultActions result = mockMvc.perform(get("/api/team")
										.accept(MediaType.APPLICATION_JSON));
		
		//then
		result.andExpect(status().isOk())
				.andDo(document("team-findTeamList",
						ApiDocumentUtils.getDocumentRequest(),
						ApiDocumentUtils.getDocumentResponse(),
						responseFields(
								fieldWithPath("[].name").description("팀 이름"),
								fieldWithPath("[].descript").description("팀 설명"),
								fieldWithPath("[].createdDate").description("팀 생성 일자"),
								fieldWithPath("[].lastModifiedDate").description("팀 정보 수정 일자")
							)
						));
		
	}
	
	@Test
	public void findTeamByNameTest() throws Exception {
		//given
		List<TeamDto.Response> dtoList = new ArrayList<>();
		dtoList.add(new TeamDto.Response(team1));
		given(teamService.findByName("testTeam1")).willReturn(dtoList);
		
		//when
		ResultActions result = mockMvc.perform(get("/api/team/testTeam1")
										.accept(MediaType.APPLICATION_JSON));
		
		//then
		result.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("testTeam1"))
				.andExpect(jsonPath("$[0].descript").value("sample1"))
				.andDo(document("team-findTeamByName",
						ApiDocumentUtils.getDocumentRequest(),
						ApiDocumentUtils.getDocumentResponse(),
						responseFields(
								fieldWithPath("[].name").description("팀 이름"),
								fieldWithPath("[].descript").description("팀 설명"),
								fieldWithPath("[].createdDate").description("팀 생성 일자"),
								fieldWithPath("[].lastModifiedDate").description("팀 정보 수정 일자")
							)
						));
		
	}
	
	@Test
	public void createTeamTest() throws Exception {
		//given
		TeamDto.CreateRequest createRequest = TeamDto.CreateRequest.builder()
																	.name("testTeam3")
																	.descript("sample3")
																	.build();
		
		//when
		ResultActions result = mockMvc.perform(post("/api/team")
										.content(objectMapper.writeValueAsString(createRequest))
										.contentType(MediaType.APPLICATION_JSON));
		
		//then
		result.andExpect(status().isCreated())
				.andDo(document("team-createTeam",
						ApiDocumentUtils.getDocumentRequest(),
						ApiDocumentUtils.getDocumentResponse(),
						requestFields(
							fieldWithPath("name").type(JsonFieldType.STRING).description("팀 이름"),
							fieldWithPath("descript").type(JsonFieldType.STRING).description("팀 설명")
							)
						));
		
	}
	
	@Test
	public void changeTeamDescriptTest() throws Exception {
		//given
		TeamDto.ChangeDescriptRequest changeDescriptRequest = TeamDto.ChangeDescriptRequest.builder()
																							.descript("changed descipt")
																							.build();
		
		//when
		ResultActions result = mockMvc.perform(patch("/api/team/descript/1")
				.content(objectMapper.writeValueAsString(changeDescriptRequest))
				.contentType(MediaType.APPLICATION_JSON));
		
		//then
		result.andExpect(status().isOk())
				.andDo(document("team-changeTeamDescript",
						ApiDocumentUtils.getDocumentRequest(),
						ApiDocumentUtils.getDocumentResponse(),
						requestFields(
							fieldWithPath("descript").type(JsonFieldType.STRING).description("팀 설명")
							)
						));
		
	};
	
	@Test
	public void deleteTeamTest() throws Exception {
		//given
		
		//when
		ResultActions result = mockMvc.perform(delete("/api/team/1"));
		
		//then
		result.andExpect(status().isOk())
				.andDo(document("team-deleteTeam",
						ApiDocumentUtils.getDocumentRequest(),
						ApiDocumentUtils.getDocumentResponse()
						));
		
	}
}
