package com.iso.logus.log;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import com.iso.logus.domain.log.domain.KeywordType;
import com.iso.logus.domain.log.domain.Log;
import com.iso.logus.domain.log.domain.LogRepository;
import com.iso.logus.domain.log.exception.LogNotFoundException;
import com.iso.logus.domain.log.service.LogSearchService;
import com.iso.logus.domain.team.domain.team.Team;
import com.iso.logus.domain.team.exception.TeamNotFoundException;
import com.iso.logus.domain.user.domain.User;
import com.iso.logus.team.TeamTestSampleData;
import com.iso.logus.user.UserTestSampleData;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class LogSearchServiceTest {
	@InjectMocks
	LogSearchService logSearchService;
	
	@Mock
	LogRepository logRepository;
	
	private static final TeamTestSampleData teamSampleData = new TeamTestSampleData();
	private static final UserTestSampleData userSampleData = new UserTestSampleData();
	private static final LogTestSampleData logSampleData = new LogTestSampleData();
	
	private static long mockTeamId = 0L;
	private static Team team;
	
	private static User user;
	
	@BeforeAll
	public static void setUp() {
		team = teamSampleData.makeTeam(mockTeamId);
		user = userSampleData.buildUser();
	}
	
	@Test
	@DisplayName("findLogById: 게시글 식별자로 특정 게시글 조회")
	public void findLogByIdTest() {
		//given
		Log log = logSampleData.buildLog(team, user);
		given(logRepository.findById(anyLong())).willReturn(Optional.of(log));
		
		//when
		Log result = logSearchService.findLogById(anyLong());
		
		//then
		assertNotNull(result);
		assertThat(result, is(log));
	}
	
	@Test
	@DisplayName("findLogById: 존재하지 않는 게시글 조회")
	public void findLogByIdTest_LogNotFound() {
		//given
		given(logRepository.findById(anyLong())).willReturn(Optional.empty());
		
		//when
		assertThrows(LogNotFoundException.class, () -> {
			Log result = logSearchService.findLogById(anyLong());
		});
		//then
	}
	
	@Test
	@DisplayName("findPageLogByTeamId: 팀 식별자로 해당 팀의 모든 게시글 조회")
	public void findPageLogByTeamIdTest() {
		//given
		List<Log> logList = logSampleData.buildLogList(team, user, 20);
		Page<Log> logs = new PageImpl<Log>(logList);
		
		given(logRepository.findAllByTeamId(anyLong(), any())).willReturn(logs);
		
		//when
		Page<Log> result = logSearchService.findPageLogByTeamId(anyLong(), any(Pageable.class));
		
		//then
		assertNotNull(result);
		assertThat(result, is(logs));
	}
	
	@Test
	@DisplayName("findPageLogByTeamId: 존재하지 않는 팀 식별자로 조회")
	public void findPageLogByTeamIdTest_TeamNotFound() {
		//given
		given(logRepository.findAllByTeamId(anyLong(), any())).willReturn(Page.empty());
		
		//when
		assertThrows(TeamNotFoundException.class, () -> {
			logSearchService.findPageLogByTeamId(anyLong(), any(Pageable.class));	
		});
		
		//then
	}
	
	@Test
	@DisplayName("findPageLogByTeamIdAndKeywordTest: 키워드 테스트, titleOnly")
	public void findPageLogByTeamIdAndKeywordTest_TitleOnly() {
		//given
		List<Log> logList = logSampleData.buildLogList(team, user, 20);
		Page<Log> logs = new PageImpl<Log>(logList);
		
		given(logRepository.findAllByTeamIdAndTitleContaining(anyLong(), anyString(), any())).willReturn(logs);
		
		//when
		Page<Log> result = logSearchService.findPageLogByTeamIdAndKeyword(anyLong(), KeywordType.titleonly, anyString(), any(Pageable.class));
		
		//then
		assertNotNull(result);
		assertThat(result, is(logs));
	}
	
	@Test
	@DisplayName("findPageLogByTeamIdAndKeywordTest: 키워드 테스트, contentOnly")
	public void findPageLogByTeamIdAndKeywordTest_ContentOnly() {
		//given
		List<Log> logList = logSampleData.buildLogList(team, user, 20);
		Page<Log> logs = new PageImpl<Log>(logList);
		
		given(logRepository.findAllByTeamIdAndContentContaining(anyLong(), anyString(), any())).willReturn(logs);
		
		//when
		Page<Log> result = logSearchService.findPageLogByTeamIdAndKeyword(anyLong(), KeywordType.contentonly, anyString(), any(Pageable.class));
		
		//then
		assertNotNull(result);
		assertThat(result, is(logs));
	}
	
	@Test
	@DisplayName("findPageLogByTeamIdAndKeywordTest: 키워드 테스트, titleandcontent")
	public void findPageLogByTeamIdAndKeywordTest_TitleAndContent() {
		//given
		List<Log> logList = logSampleData.buildLogList(team, user, 20);
		Page<Log> logs = new PageImpl<Log>(logList);
		
		given(logRepository.findAllByTeamIdAndKeywordContaining(anyLong(), anyString(), any())).willReturn(logs);
		
		//when
		Page<Log> result = logSearchService.findPageLogByTeamIdAndKeyword(anyLong(), KeywordType.titleandcontent, anyString(), any(Pageable.class));
		
		//then
		assertNotNull(result);
		assertThat(result, is(logs));
	}
	
	@Test
	@DisplayName("findPageLogByTeamIdAndKeywordTest: 키워드 테스트, author")
	public void findPageLogByTeamIdAndKeywordTest_Author() {
		//given
		List<Log> logList = logSampleData.buildLogList(team, user, 20);
		Page<Log> logs = new PageImpl<Log>(logList);
		
		given(logRepository.findAllByTeamIdAndAuthorContaining(anyLong(), anyString(), any())).willReturn(logs);
		
		//when
		Page<Log> result = logSearchService.findPageLogByTeamIdAndKeyword(anyLong(), KeywordType.author, anyString(), any(Pageable.class));
		
		//then
		assertNotNull(result);
		assertThat(result, is(logs));
	}
	
	
}
