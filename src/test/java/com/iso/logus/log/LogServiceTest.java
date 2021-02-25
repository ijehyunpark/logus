package com.iso.logus.log;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import com.iso.logus.domain.log.domain.Log;
import com.iso.logus.domain.log.domain.LogRepository;
import com.iso.logus.domain.log.dto.LogDto;
import com.iso.logus.domain.log.service.LogSearchService;
import com.iso.logus.domain.log.service.LogService;
import com.iso.logus.domain.team.domain.team.Team;
import com.iso.logus.domain.team.service.TeamSearchService;
import com.iso.logus.domain.user.domain.User;
import com.iso.logus.domain.user.service.UserService;
import com.iso.logus.team.TeamTestSampleData;
import com.iso.logus.user.UserTestSampleData;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class LogServiceTest {
	@InjectMocks
	LogService logService;
	
	@Mock
	LogRepository logRepository;
	
	@Mock
	TeamSearchService teamSearchService;
	
	@Mock
	UserService userService;
		
	@Mock
	LogSearchService logSearchService;
	
	private final TeamTestSampleData teamSampleData = new TeamTestSampleData();
	private final UserTestSampleData userSampleData = new UserTestSampleData();
	private final LogTestSampleData logSampleData = new LogTestSampleData();
	
	private long teamMockId = 0L;
	private Team team;
	
	private User user;
	
	@BeforeEach
	public void setUp() {
		team = teamSampleData.makeTeam(teamMockId);
		
		user = userSampleData.buildUser();
	}
	
	@Test
	@DisplayName("writeLog:게시글 작성, 부모글을 가지지 않는 경우")
	public void writeLogTest_ParentIdNull() {
		//given
		LogDto.LogCreateRequest createRequest = logSampleData.buildCreateRequest(team.getId(), user.getUid());
		Log log = createRequest.toEntity(team, user, null);
		
		given(teamSearchService.findTeamById(anyLong())).willReturn(team);
		given(userService.findUserByUid(anyString())).willReturn(user);
		given(logRepository.save(any(Log.class))).willReturn(log);
		
		//when
		Log result = logService.writeLog(createRequest);
		
		//then
		assertNotNull(result);
		assertThat(result, is(log));
	}
	
	@Test
	@DisplayName("writeLog:게시글 작성, 부모글을 가진 경우")
	public void writeLogTest_ParentIdNotNull() {
		//given
		long parentLogMockId = 400;
		Log parentLog = logSampleData.buildLog(team, user, parentLogMockId);
		logSampleData.setMockLogId(parentLog, parentLogMockId);
		LogDto.LogCreateRequest createRequest = LogDto.LogCreateRequest.builder()
									.teamId(team.getId())
									.author(user.getUid())
									.title("Sample Title")
									.content("Sample Content")
									.parentLogId(parentLog.getId())
									.build();
		Log log = createRequest.toEntity(team, user, parentLog);
		
		given(teamSearchService.findTeamById(anyLong())).willReturn(team);
		given(userService.findUserByUid(anyString())).willReturn(user);
		given(logSearchService.findLogById(anyLong())).willReturn(parentLog);
		given(logRepository.save(any(Log.class))).willReturn(log);
		
		//when
		Log result = logService.writeLog(createRequest);
		
		//then
		assertNotNull(result);
		assertThat(result, is(log));
	}
	
	@Test
	@DisplayName("changeLog: 게시글 수정")
	public void changeLogTest() {
		//given
		Log log = logSampleData.buildLog(team, user);
		logSampleData.setMockLogId(log, 0L);
		LogDto.ChangeRequest changeRequest = LogDto.ChangeRequest.builder()
				.logId(log.getId())
				.title("Changed Title")
				.content("Changed Content")
				.build();
		
		given(logSearchService.findLogById(anyLong())).willReturn(log);
		
		//when
		logService.changeLog(changeRequest);
		
		//then
		assertThat(log.getTitle(), is(changeRequest.getTitle()));
		assertThat(log.getContent(), is(changeRequest.getContent()));
	}
	
	@Test
	@DisplayName("deleteLog: 게시글 삭제, 일반 게시글")
	public void deleteLogTest() {
		//given
		Log log = logSampleData.buildLog(team, user);
		given(logSearchService.findLogById(anyLong())).willReturn(log);
		
		//when
		logService.deleteLog(anyLong());
		
		//then
		verify(logRepository, times(1)).delete(log);
	}
	
	@Test
	@DisplayName("deleteLog: 게시글 삭제, 연관된 자식 글의 모든 부모글 참조 삭제")
	public void deleteLogTest_ParentLogDelete() {
		//given
		long parentLogMockId = 400L;
		Log log = logSampleData.buildLog(team, user);
		logSampleData.setMockLogId(log, parentLogMockId);
		
		Log childLog = LogDto.LogCreateRequest.builder()
				.teamId(team.getId())
				.author(user.getUid())
				.title("Changed Title")
				.content("Changed Content")
				.parentLogId(parentLogMockId)
				.build()
				.toEntity(team, user, log);
		Set<Log> childLogList = Set.of(childLog);
		ReflectionTestUtils.setField(log, "childLogs", childLogList);
		given(logSearchService.findLogById(anyLong())).willReturn(log);
		
		//when
		logService.deleteLog(anyLong());
		
		//then
		assertNull(childLog.getParentLog());
		verify(logRepository, times(1)).delete(log);
	}
}
