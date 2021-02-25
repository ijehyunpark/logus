package com.iso.logus.log;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import com.iso.logus.ApiDocumentUtils;
import com.iso.logus.ControllerTest;
import com.iso.logus.domain.log.domain.KeywordType;
import com.iso.logus.domain.log.domain.Log;
import com.iso.logus.domain.log.dto.LogDto;
import com.iso.logus.domain.log.exception.LogNotFoundException;
import com.iso.logus.domain.log.service.LogSearchService;
import com.iso.logus.domain.log.service.LogService;
import com.iso.logus.domain.team.domain.team.Team;
import com.iso.logus.domain.team.exception.TeamNotFoundException;
import com.iso.logus.domain.user.domain.User;
import com.iso.logus.global.error.ErrorCode;
import com.iso.logus.team.TeamTestSampleData;
import com.iso.logus.user.UserTestSampleData;

public class LogControllerTest extends ControllerTest {
	
	@MockBean
	private LogService logService;
	
	@MockBean
	private LogSearchService logSearchService;
	
	private final TeamTestSampleData teamSampleData = new TeamTestSampleData();
	private final UserTestSampleData userSampleData = new UserTestSampleData();
	private final LogTestSampleData logSampleData = new LogTestSampleData();
	
	private long mockTeamId = 0L;
	private Team team;
	
	private long mockUserNum = 0L;
	private User user;
	
	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider) {
		setUpMockMvc(webApplicationContext, restDocumentationContextProvider);
	
		team = teamSampleData.makeTeam(mockTeamId);
		user = userSampleData.buildUser(mockUserNum);
	}
	
	@Test
	public void findLogTest() throws Exception {
		//given
		LogDto.LogCreateRequest createRequest = logSampleData.buildCreateRequest(team.getId(), user.getUid());
		Log log = createRequest.toEntity(team, user, null);
		ReflectionTestUtils.setField(log, "id", 0L);
		given(logSearchService.findLogById(anyLong())).willReturn(log);
		
		//when
		ResultActions result = performGet("/api/log/" + team.getId());
		
		//then
		result.andExpect(status().isOk())
				.andExpect(jsonPath("$.teamId", is(createRequest.getTeamId()), Long.class))
				.andExpect(jsonPath("$.author", is(createRequest.getAuthor())))
				.andExpect(jsonPath("$.title", is(createRequest.getTitle())))
				.andExpect(jsonPath("$.content", is(createRequest.getContent())))
				.andExpect(jsonPath("$.parentLogId", is(createRequest.getParentLogId()), Long.class))
				.andDo(document("log-findLog",
						ApiDocumentUtils.getDocumentRequest(),
						ApiDocumentUtils.getDocumentResponse(),
						responseFields(
								fieldWithPath("id").description("글 식별자"),
								fieldWithPath("teamId").description("팀 식별자"),
								fieldWithPath("author").description("글 작성자의 아이디"),
								fieldWithPath("title").description("글 제목"),
								fieldWithPath("content").description("글 내용"),
								fieldWithPath("parentLogId").description("부모글 식별자")
							)
						));
	}
	
	@Test
	@Disabled
	@DisplayName("findLog: 잘못된 글 식별자 요청")
	public void findLogTest_LogNotFound() throws Exception {
		//given
		given(logSearchService.findLogById(anyLong())).willThrow(LogNotFoundException.class);
		
		//when
		ResultActions result = performGet("/api/log/" + team.getId());
		
		//then
		result.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.code", is(ErrorCode.LOG_NOT_FOUND.getCode())))
				.andExpect(jsonPath("$.message", is(ErrorCode.LOG_NOT_FOUND.getMessage())))
				.andDo(document("log-findLog_LogNotFound",
						ApiDocumentUtils.getDocumentRequest(),
						ApiDocumentUtils.getDocumentResponse(),
						responseFields(
							ApiDocumentUtils.commonErrorField()
							)
						));
	}
	
	@Test
	public void findPageLogTest() throws Exception {
		//given
		int PAGESIZE = 20;
		List<Log> logList = logSampleData.buildLogList(team,user,PAGESIZE);
		
		Page<Log> logs = new PageImpl<Log>(logList);
		given(logSearchService.findPageLogByTeamId(anyLong(), any(Pageable.class))).willReturn(logs);
		
		MultiValueMap<String, String> params = ApiDocumentUtils.buildPageParameter();
		
		//when
		ResultActions result = performGet("/api/log/page/" + team.getId(), params);
		
		//then
		result.andExpect(status().isOk())
				.andDo(document("log-findPageLog",
						ApiDocumentUtils.getDocumentRequest(),
						ApiDocumentUtils.getDocumentResponse(),
						requestParameters(
								ApiDocumentUtils.pageParameter()
								),
						responseFields(
								ApiDocumentUtils.pageField()
								)
							.and(	
								fieldWithPath("content[].id").description("글 식별자"),
								fieldWithPath("content[].teamId").description("팀 식별자"),
								fieldWithPath("content[].author").description("글 작성자의 아이디"),
								fieldWithPath("content[].title").description("글 제목"),
								fieldWithPath("content[].content").description("글 내용"),
								fieldWithPath("content[].parentLogId").description("부모글 식별자")
							)
						));
	}
	
	@Test
	@Disabled
	@DisplayName("findPageLog: 잘못된 팀 식별자 요청")
	public void findPageLogTest_TeamNotFound() throws Exception {
		//given
		given(logSearchService.findPageLogByTeamId(anyLong(), any(Pageable.class))).willThrow(TeamNotFoundException.class);
		
		//when
		ResultActions result = performGet("/api/log/page/" + team.getId());
		
		//then
		result.andExpect(status().isNotFound())
				.andDo(document("log-findPageLog_TeamNotFound",
						ApiDocumentUtils.getDocumentRequest(),
						ApiDocumentUtils.getDocumentResponse(),
						responseFields(
							ApiDocumentUtils.commonErrorField()
							)
						));
	}
	
	@Test
	public void findPageLogByKeywordTest() throws Exception {
		//given
		int PAGESIZE = 20;
		List<Log> logList = logSampleData.buildLogList(team,user,PAGESIZE);
		
		Page<Log> logs = new PageImpl<Log>(logList);
		
		MultiValueMap<String, String> params = ApiDocumentUtils.buildPageParameter();
		given(logSearchService.findPageLogByTeamIdAndKeyword(anyLong(), any(KeywordType.class), anyString(), any(Pageable.class))).willReturn(logs);
		
		//when
		ResultActions result = performGet("/api/log/find/author/" + team.getId() + "/" + user.getUid(), params);
		//then
		result.andExpect(status().isOk())
				.andDo(document("log-findPageLog",
						ApiDocumentUtils.getDocumentRequest(),
						ApiDocumentUtils.getDocumentResponse(),
						requestParameters(
								ApiDocumentUtils.pageParameter()
								),
						responseFields(
								ApiDocumentUtils.pageField()
								)
							.and(		
								fieldWithPath("content[].id").description("글 식별자"),
								fieldWithPath("content[].teamId").description("팀 식별자"),
								fieldWithPath("content[].author").description("글 작성자의 아이디"),
								fieldWithPath("content[].title").description("글 제목"),
								fieldWithPath("content[].content").description("글 내용"),
								fieldWithPath("content[].parentLogId").description("부모글 식별자")
							)
						));
	}
	
	@Test
	public void wirteLogTest() throws Exception {
		//given
		LogDto.LogCreateRequest createRequest = logSampleData.buildCreateRequest(team.getId(), user.getUid());
		
		//when
		ResultActions result = performPost("/api/log", createRequest);
		
		//then
		result.andExpect(status().isCreated())
				.andDo(document("log-wirteLog",
						ApiDocumentUtils.getDocumentRequest(),
						ApiDocumentUtils.getDocumentResponse(),
						requestFields(
							fieldWithPath("teamId").type(JsonFieldType.NUMBER).description("팀 식별자"),
							fieldWithPath("author").type(JsonFieldType.STRING).description("작성자 아이디"),
							fieldWithPath("title").type(JsonFieldType.STRING).description("글 제목"),
							fieldWithPath("content").type(JsonFieldType.STRING).description("글 내용"),
							fieldWithPath("parentLogId").type(JsonFieldType.NUMBER).description("부모글 식별자").optional()
							)
						));
	}
	
	@Test
	public void changeLogTest() throws Exception {
		//given
		LogDto.ChangeRequest changeRequest = LogDto.ChangeRequest.builder()
				.logId(anyLong())
				.title("changed Title")
				.content("changed Content")
				.build();
		
		//when
		ResultActions result = performPatch("/api/log", changeRequest);
		
		//then
		result.andExpect(status().isOk())
				.andDo(document("log-changeLog",
						ApiDocumentUtils.getDocumentRequest(),
						ApiDocumentUtils.getDocumentResponse(),
						requestFields(
							fieldWithPath("logId").type(JsonFieldType.NUMBER).description("게시글 식별자"),
							fieldWithPath("title").type(JsonFieldType.STRING).description("글 제목"),
							fieldWithPath("content").type(JsonFieldType.STRING).description("글 내용")
							)
						));
	}
	
	@Test
	public void deleteLogTest() throws Exception {
		//given
		long mockLogId = 0L;
		
		//when
		ResultActions result = performDelete("/api/log/" + mockLogId);
		
		//then
		result.andExpect(status().isOk())
				.andDo(document("log-deleteLog",
						ApiDocumentUtils.getDocumentRequest(),
						ApiDocumentUtils.getDocumentResponse()
						));
	}
}
