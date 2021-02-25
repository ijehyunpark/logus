package com.iso.logus.team.teamuser;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import com.iso.logus.ApiDocumentUtils;
import com.iso.logus.ControllerTest;
import com.iso.logus.domain.team.domain.team.Team;
import com.iso.logus.domain.team.domain.teamauth.AuthType;
import com.iso.logus.domain.team.domain.teamauth.TeamAuth;
import com.iso.logus.domain.team.domain.teamuser.TeamUser;
import com.iso.logus.domain.team.dto.TeamUserDto;
import com.iso.logus.domain.team.service.TeamAuthBaseData;
import com.iso.logus.domain.team.service.TeamUserService;
import com.iso.logus.domain.user.domain.User;
import com.iso.logus.team.TeamTestSampleData;
import com.iso.logus.user.UserTestSampleData;

public class TeamUserControllerTest extends ControllerTest {

	@MockBean
	private TeamUserService teamUserService;
	
	private TeamTestSampleData teamSampleData = new TeamTestSampleData();
	private UserTestSampleData userSampleData = new UserTestSampleData();
	private TeamAuthBaseData authBaseData = new TeamAuthBaseData();
	
	/*
	 * masterUser: team의 최종 권한을 가진 유저의 예제
	 * user{1, 2}: team의 일반 유저의 예제
	 * user{3}: team에 소속되지 않은 유저
	 */
	private long mockTeamId = 0L;
	private Team team;
	private TeamAuth masterAuth, defaultAuth;
	private User masterUser, user1, user2, user3;
	private TeamUser masterTeamUser, teamUser1, teamUser2;
	
	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider) {
		setUpMockMvc(webApplicationContext, restDocumentationContextProvider);
		
		team = teamSampleData.makeTeam(mockTeamId);
		masterAuth = authBaseData.createMasterAuth(team);
		defaultAuth = authBaseData.createDefaultAuth(team);
		masterUser = userSampleData.buildUser(0L);
		user1 = userSampleData.buildUser(1L);
		user2 = userSampleData.buildUser(2L);
		user3 = userSampleData.buildUser(3L);
		
		masterTeamUser = TeamUserDto.JoinRequest.builder()
								.teamId(team.getId())
								.uid(masterUser.getUid())
								.customName("masterUser")
								.build()
								.toEntity(team, masterUser, masterAuth);
		
		teamUser1 = TeamUserDto.JoinRequest.builder()
								.teamId(team.getId())
								.uid(user1.getUid())
								.build()
								.toEntity(team, user1, defaultAuth);
		
		teamUser2 = TeamUserDto.JoinRequest.builder()
								.teamId(team.getId())
								.uid(user2.getUid())
								.build()
								.toEntity(team, user2, defaultAuth);
	}
	
	@Test
	public void findTeamUserAuthTest() throws Exception {
		//given
		given(teamUserService.isUserHasAuth(mockTeamId, "anyString", AuthType.authmanageauth)).willReturn(true);
		
		//when
		ResultActions result = mockMvc.perform(get("/api/team/member/auth-test/" + mockTeamId + "/anyString/authmanageauth")
				.accept(MediaType.APPLICATION_JSON));
		
		//then
		result.andExpect(status().isOk())
				.andDo(document("teamUser-isUserHasAuth",
						ApiDocumentUtils.getDocumentRequest(),
						ApiDocumentUtils.getDocumentResponse(),
						responseFields(
								fieldWithPath("value").description("권한 여부")
							)
						));
	}
	
	@Test
	public void findAllMemberByTeamTest() throws Exception{
		//given
		List<TeamUserDto.MemberResponse> dtoList = new ArrayList<>();
		dtoList.add(new TeamUserDto.MemberResponse(masterTeamUser));
		dtoList.add(new TeamUserDto.MemberResponse(teamUser1));
		dtoList.add(new TeamUserDto.MemberResponse(teamUser2));
		given(teamUserService.findAllMemberByTeam(mockTeamId)).willReturn(dtoList);
		
		//when
		ResultActions result = mockMvc.perform(get("/api/team/member/find/member/" + mockTeamId)
										.accept(MediaType.APPLICATION_JSON));
		
		//then
		result.andExpect(status().isOk())
				.andExpect(jsonPath("$..uid", Matchers.contains("user0", "user1", "user2")))
				.andDo(document("teamUser-findAllMemberByTeam",
						ApiDocumentUtils.getDocumentRequest(),
						ApiDocumentUtils.getDocumentResponse(),
						responseFields(
								fieldWithPath("[].uid").description("유저 아이디"),
								fieldWithPath("[].teamAuthName").description("팀에서의 권한"),
								fieldWithPath("[].teamMaster").description("팀 최종 관리자 여부"),
								fieldWithPath("[].customName").description("팀에서 사용하는 이름").optional()
							)
						));
	}
	
	@Test
	public void findAllTeamByUser() throws Exception {
		//given
		List<TeamUserDto.TeamResponse> dtoList = new ArrayList<>();
		dtoList.add(new TeamUserDto.TeamResponse(teamUser1));
		given(teamUserService.findAllTeamByUser("user1")).willReturn(dtoList);
		
		//when
		ResultActions result = mockMvc.perform(get("/api/team/member/find/team/user1")
								.accept(MediaType.APPLICATION_JSON));
		
		//then
		result.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(1)))
				.andDo(document("teamUser-findAllTeamByUser",
						ApiDocumentUtils.getDocumentRequest(),
						ApiDocumentUtils.getDocumentResponse(),
						responseFields(
								fieldWithPath("[].team.id").description("팀 식별자"),
								fieldWithPath("[].team.name").description("팀 이름"),
								fieldWithPath("[].team.descript").description("팀 설명"),
								fieldWithPath("[].team.createdDate").description("팀 생성 일자"),
								fieldWithPath("[].team.lastModifiedDate").description("팀 사항 변경 일자")
							)
						));
	}
	
	@Test
	public void joinNewMember() throws Exception {
		//given
		TeamUserDto.JoinRequest joinRequest = TeamUserDto.JoinRequest.builder()
				.teamId(team.getId())
				.uid(user3.getUid())
				.build();
		
		//when
		ResultActions result = mockMvc.perform(post("/api/team/member/join")
									.content(objectMapper.writeValueAsString(joinRequest))
									.contentType(MediaType.APPLICATION_JSON));
		
		//then
		result.andExpect(status().isOk())
				.andDo(document("teamUser-joinNewMember",
						ApiDocumentUtils.getDocumentRequest(),
						ApiDocumentUtils.getDocumentResponse(),
						requestFields(
							fieldWithPath("teamId").type(JsonFieldType.NUMBER).description("팀 식별자"),
							fieldWithPath("uid").type(JsonFieldType.STRING).description("유저 아이디"),
							fieldWithPath("customName").type(JsonFieldType.STRING).description("팀에서 사용할 이름").optional()
							)
						));
	}
	
	@Test
	public void quitMember() throws Exception {
		//given
		String token = jwtTokenProvider.createToken("uid");
		
		given(teamUserService.isUserHasMasterAuth(mockTeamId, "uid")).willReturn(true);
		
		TeamUserDto.QuitRequest quitRequest = TeamUserDto.QuitRequest.builder()
				.teamId(team.getId())
				.uid(user3.getUid())
				.build();
		
		//when
		ResultActions result = mockMvc.perform(post("/api/team/member/quit")
									.content(objectMapper.writeValueAsString(quitRequest))
									.contentType(MediaType.APPLICATION_JSON)
									.header("X-AUTH-TOKEN", token));
		
		//then
		result.andExpect(status().isOk())
				.andDo(document("teamUser-quitMember",
						ApiDocumentUtils.getDocumentRequest(),
						ApiDocumentUtils.getDocumentResponse(),
						requestFields(
							fieldWithPath("teamId").type(JsonFieldType.NUMBER).description("팀 식별자"),
							fieldWithPath("uid").type(JsonFieldType.STRING).description("유저 아이디")
							)
						));
	}
	
	@Test
	public void changeAuthTest() throws Exception {
		//given
		String token = jwtTokenProvider.createToken("uid");
		
		given(teamUserService.isUserHasAuth(mockTeamId, "uid", AuthType.authmanageauth)).willReturn(true);
		
		TeamUserDto.ChangeAuthRequest changeAuthRequest = TeamUserDto.ChangeAuthRequest.builder()
				.teamId(team.getId())
				.uid(user1.getUid())
				.teamAuthName("custom-rank")
				.build();
		
		//when
		ResultActions result = mockMvc.perform(patch("/api/team/member/auth")
									.content(objectMapper.writeValueAsString(changeAuthRequest))
									.contentType(MediaType.APPLICATION_JSON)
									.header("X-AUTH-TOKEN", token));
									
		//then
		result.andExpect(status().isOk())
				.andDo(document("teamUser-changeAuth",
						ApiDocumentUtils.getDocumentRequest(),
						ApiDocumentUtils.getDocumentResponse(),
						requestFields(
							fieldWithPath("teamId").type(JsonFieldType.NUMBER).description("팀 식별자"),
							fieldWithPath("uid").type(JsonFieldType.STRING).description("유저 아이디"),
							fieldWithPath("teamAuthName").type(JsonFieldType.STRING).description("팀 권한 이름")
							)
						));
	}
	
	@Test
	public void changeCustomName() throws Exception {
		//given
		TeamUserDto.ChangeCustomNameRequest customNameRequest = TeamUserDto.ChangeCustomNameRequest.builder()
				.teamId(team.getId())
				.uid(user1.getUid())
				.build();
		
		//when
		ResultActions result = mockMvc.perform(patch("/api/team/member/name")
									.content(objectMapper.writeValueAsString(customNameRequest))
									.contentType(MediaType.APPLICATION_JSON));
		
		//then
		result.andExpect(status().isOk())
				.andDo(document("teamUser-changeAuth",
						ApiDocumentUtils.getDocumentRequest(),
						ApiDocumentUtils.getDocumentResponse(),
						requestFields(
							fieldWithPath("teamId").type(JsonFieldType.NUMBER).description("팀 식별자"),
							fieldWithPath("uid").type(JsonFieldType.STRING).description("유저 아이디"),
							fieldWithPath("customName").type(JsonFieldType.STRING).description("팀에서 사용할 이름").optional()
							)
						));
	}
}
