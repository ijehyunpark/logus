package com.iso.logus.team.team;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import com.iso.logus.ApiDocumentUtils;
import com.iso.logus.ControllerTest;
import com.iso.logus.domain.team.domain.team.Team;
import com.iso.logus.domain.team.domain.team.TeamRepository;
import com.iso.logus.domain.team.dto.TeamDto;
import com.iso.logus.domain.team.service.TeamSearchService;
import com.iso.logus.domain.team.service.TeamService;
import com.iso.logus.domain.user.domain.User;
import com.iso.logus.domain.user.domain.UserRepository;
import com.iso.logus.domain.user.dto.UserDto;

public class TeamControllerTest extends ControllerTest {
	
	@MockBean
	private TeamService teamService;
	
	@MockBean
	private TeamSearchService teamSearchService;
	
	@Autowired
	private TeamRepository teamRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	int TEAM_NUM = 0;
	
	private Team team1, team2;
	private User masterUser;
	
	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider) {
		setUpMockMvc(webApplicationContext, restDocumentationContextProvider);
		
		masterUser = UserDto.SignUpRequest.builder()
							.uid("master")
							.name("master")
							.password("password")
							.build()
							.toEntity();
		userRepository.save(masterUser);
		
		team1 = createRequestBuilder().toEntity();
		team2 = createRequestBuilder().toEntity();
		teamRepository.save(team1);
		teamRepository.save(team2);
	}
	
	@Test
	public void findTeamListTest() throws Exception {
		//given
		List<TeamDto.Response> dtoList = new ArrayList<>();
		dtoList.add(new TeamDto.Response(team1));
		dtoList.add(new TeamDto.Response(team2));
		given(teamSearchService.findList()).willReturn(dtoList);
		
		//when
		ResultActions result = mockMvc.perform(get("/api/team")
										.accept(MediaType.APPLICATION_JSON));
		
		//then
		result.andExpect(status().isOk())
				.andDo(document("team-findTeamList",
						ApiDocumentUtils.getDocumentRequest(),
						ApiDocumentUtils.getDocumentResponse(),
						responseFields(
								fieldWithPath("[].id").description("팀 식별자"),
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
		given(teamSearchService.findByName("testTeam1")).willReturn(dtoList);
		
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
								fieldWithPath("[].id").description("팀 식별자"),
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
		TeamDto.CreateRequest createRequest = createRequestBuilder();
		
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
	public void changeTeamTest() throws Exception {
		//given
		TeamDto.UpdateRequest updateRequest = TeamDto.UpdateRequest.builder()
																	.name("changed name")
																	.descript("changed descipt")
																	.build();
		
		//when
		ResultActions result = mockMvc.perform(patch("/api/team/1")
				.content(objectMapper.writeValueAsString(updateRequest))
				.contentType(MediaType.APPLICATION_JSON));
		
		//then
		result.andExpect(status().isOk())
				.andDo(document("team-changeTeam",
						ApiDocumentUtils.getDocumentRequest(),
						ApiDocumentUtils.getDocumentResponse(),
						requestFields(
							fieldWithPath("name").type(JsonFieldType.STRING).description("팀 이름"),
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
	
	private TeamDto.CreateRequest createRequestBuilder() {
		TEAM_NUM++;
		return TeamDto.CreateRequest.builder()
				.name("testTeam" + String.valueOf(TEAM_NUM))
				.descript("sample" + String.valueOf(TEAM_NUM))
				.build();
	}
}
