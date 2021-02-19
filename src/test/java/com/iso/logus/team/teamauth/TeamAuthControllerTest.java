package com.iso.logus.team.teamauth;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import com.iso.logus.domain.team.domain.teamauth.AuthType;
import com.iso.logus.domain.team.domain.teamauth.TeamAuthType;
import com.iso.logus.domain.team.dto.TeamAuthDto;
import com.iso.logus.domain.team.service.TeamAuthService;
import com.iso.logus.domain.team.service.TeamUserService;
import com.iso.logus.team.TeamTestSampleData;

public class TeamAuthControllerTest extends ControllerTest {
	
	@MockBean
	private TeamAuthService teamAuthService;
	
	@MockBean 
	private TeamUserService teamUserService;
	
	@Autowired
	private TeamTestSampleData sampleData;
	
	private Team team;
	
	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider) {
		setUpMockMvc(webApplicationContext, restDocumentationContextProvider);
		
		team = sampleData.makeTeam(1L);
	}

	@Test
	public void findTeamAuthListTest() throws Exception {
		//given
		List<TeamAuthDto.Response> dtoList = new ArrayList<>();
		dtoList.add(new TeamAuthDto.Response(sampleData.returnAllTrueAuth(team,"First Rank")));
		dtoList.add(new TeamAuthDto.Response(sampleData.returnAllFalseAuth(team,"Normal Rank")));
		dtoList.add(new TeamAuthDto.Response(sampleData.returnSampleAuth(team, "Custom Rank")));
		given(teamAuthService.findList(anyLong())).willReturn(dtoList);
		
		//when
		ResultActions result = mockMvc.perform(get("/api/team/auth/1")
				.accept(MediaType.APPLICATION_JSON));
		
		//then
		result.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", is(3)))
				.andDo(document("teamAuth-findTeamAuthList",
						ApiDocumentUtils.getDocumentRequest(),
						ApiDocumentUtils.getDocumentResponse(),
						responseFields(
								fieldWithPath("[].name").description("권한 이름"),
								fieldWithPath("[].type").description("팀 권한 분류"),
								fieldWithPath("[].masterAuth.teamNameAuth").description("팀 이름 변경 권한 여부"),
								fieldWithPath("[].masterAuth.authManageAuth").description("팀 권한 관리 권한 여부"),
								fieldWithPath("[].masterAuth.logMasterAuth").description("모든 로그 조작 권한"),
								fieldWithPath("[].masterAuth.calendarMasterAuth").description("모든 캘린더 조작 권한"),
								fieldWithPath("[].memberControllAuth.inviteAuth").description("팀 초대 권한 여부"),
								fieldWithPath("[].memberControllAuth.inviteAcceptAuth").description("팀 초대 수락 권한 여부"),
								fieldWithPath("[].memberControllAuth.quitAuth").description("팀 추방 권한 여부"),
								fieldWithPath("[].activeAuth.logWriteAuth").description("글 쓰기 권한"),
								fieldWithPath("[].activeAuth.replyWriteAuth").description("댓글 쓰기 권한"),
								fieldWithPath("[].activeAuth.toDoAuth").description("toDoList 조작 권한"),
								fieldWithPath("[].activeAuth.calendarAuth").description("캘린더 조작 권한")
							)
						));
	}
	
	@Test
	public void createTeamAuthTest() throws Exception {
		//given
		String token = jwtTokenProvider.createToken("uid");
		
		given(teamUserService.isUserHasAuth(1L, "uid", AuthType.authManageAuth)).willReturn(true);
		TeamAuthDto.SaveRequest saveRequest = sampleData.saveRequestBuilder(1);
		
		//when
		ResultActions result = mockMvc.perform(post("/api/team/auth")
										.content(objectMapper.writeValueAsString(saveRequest))
										.contentType(MediaType.APPLICATION_JSON)
										.header("X-AUTH-TOKEN", token));
		
		//then
		result.andExpect(status().isCreated())
				.andDo(document("teamAuth-createTeamAuth",
						ApiDocumentUtils.getDocumentRequest(),
						ApiDocumentUtils.getDocumentResponse(),
						requestFields(
							fieldWithPath("teamId").type(JsonFieldType.NUMBER).description("팀 식별자"),	
							fieldWithPath("name").type(JsonFieldType.STRING).description("권한 이름"),
							fieldWithPath("masterAuth.teamNameAuth").type(JsonFieldType.BOOLEAN).description("팀 이름 변경 권한 여부").optional(),
							fieldWithPath("masterAuth.authManageAuth").type(JsonFieldType.BOOLEAN).description("팀 권한 관리 권한 여부").optional(),
							fieldWithPath("masterAuth.logMasterAuth").type(JsonFieldType.BOOLEAN).description("모든 로그 조작 권한").optional(),
							fieldWithPath("masterAuth.calendarMasterAuth").type(JsonFieldType.BOOLEAN).description("모든 캘린더 조작 권한").optional(),
							fieldWithPath("memberControllAuth.inviteAuth").type(JsonFieldType.BOOLEAN).description("팀 초대 권한 여부").optional(),
							fieldWithPath("memberControllAuth.inviteAcceptAuth").type(JsonFieldType.BOOLEAN).description("팀 초대 수락 권한 여부").optional(),
							fieldWithPath("memberControllAuth.quitAuth").type(JsonFieldType.BOOLEAN).description("팀 추방 권한 여부").optional(),
							fieldWithPath("activeAuth.logWriteAuth").type(JsonFieldType.BOOLEAN).description("글 쓰기 권한").optional(),
							fieldWithPath("activeAuth.replyWriteAuth").type(JsonFieldType.BOOLEAN).description("댓글 쓰기 권한").optional(),
							fieldWithPath("activeAuth.toDoAuth").type(JsonFieldType.BOOLEAN).description("toDoList 조작 권한").optional(),
							fieldWithPath("activeAuth.calendarAuth").type(JsonFieldType.BOOLEAN).description("캘린더 조작 권한").optional()
							)
						));
		
	}
	
	@Test
	public void changeTeamAuthTest() throws Exception {
		//given
		String token = jwtTokenProvider.createToken("uid");
		
		given(teamUserService.isUserHasAuth(1L, "uid", AuthType.authManageAuth)).willReturn(true);
		TeamAuthDto.UpdateRequest updateRequest = sampleData.updateRequestBuilder(1, "anyString", "changed auth name", TeamAuthType.NONE);
		
		//when
		ResultActions result = mockMvc.perform(put("/api/team/auth")
				.content(objectMapper.writeValueAsString(updateRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.header("X-AUTH-TOKEN", token));
		
		//then
		result.andExpect(status().isOk())
				.andDo(document("teamAuth-changeTeamAuth",
						ApiDocumentUtils.getDocumentRequest(),
						ApiDocumentUtils.getDocumentResponse(),
						requestFields(
							fieldWithPath("teamId").type(JsonFieldType.NUMBER).description("팀 식별자"),
							fieldWithPath("originName").type(JsonFieldType.STRING).description("기존 권한 이름"),
							fieldWithPath("changeName").type(JsonFieldType.STRING).description("변경된 권한 이름"),
							fieldWithPath("type").type(JsonFieldType.STRING).description("팀 권한 분류"),
							fieldWithPath("masterAuth.teamNameAuth").type(JsonFieldType.BOOLEAN).description("팀 이름 변경 권한 여부").optional(),
							fieldWithPath("masterAuth.authManageAuth").type(JsonFieldType.BOOLEAN).description("팀 권한 관리 권한 여부").optional(),
							fieldWithPath("masterAuth.logMasterAuth").type(JsonFieldType.BOOLEAN).description("모든 로그 조작 권한").optional(),
							fieldWithPath("masterAuth.calendarMasterAuth").type(JsonFieldType.BOOLEAN).description("모든 캘린더 조작 권한").optional(),
							fieldWithPath("memberControllAuth.inviteAuth").type(JsonFieldType.BOOLEAN).description("팀 초대 권한 여부").optional(),
							fieldWithPath("memberControllAuth.inviteAcceptAuth").type(JsonFieldType.BOOLEAN).description("팀 초대 수락 권한 여부").optional(),
							fieldWithPath("memberControllAuth.quitAuth").type(JsonFieldType.BOOLEAN).description("팀 추방 권한 여부").optional(),
							fieldWithPath("activeAuth.logWriteAuth").type(JsonFieldType.BOOLEAN).description("글 쓰기 권한").optional(),
							fieldWithPath("activeAuth.replyWriteAuth").type(JsonFieldType.BOOLEAN).description("댓글 쓰기 권한").optional(),
							fieldWithPath("activeAuth.toDoAuth").type(JsonFieldType.BOOLEAN).description("toDoList 조작 권한").optional(),
							fieldWithPath("activeAuth.calendarAuth").type(JsonFieldType.BOOLEAN).description("캘린더 조작 권한").optional()
							)
						));
	}
	
	@Test
	public void deleteTeamAuthTest() throws Exception {
		//given
		String token = jwtTokenProvider.createToken("uid");
		
		given(teamUserService.isUserHasAuth(1L, "uid", AuthType.authManageAuth)).willReturn(true);
		
		//when
		ResultActions result = mockMvc.perform(delete("/api/team/auth/1/anyString")
										.header("X-AUTH-TOKEN", token));
		
		//then
		result.andExpect(status().isOk())
				.andDo(document("teamAuth-deleteTeamAuth",
						ApiDocumentUtils.getDocumentRequest(),
						ApiDocumentUtils.getDocumentResponse()
						));
	}
}
