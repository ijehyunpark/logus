package com.iso.logus.team.teamauth;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.iso.logus.domain.team.domain.team.Team;
import com.iso.logus.domain.team.domain.team.TeamRepository;
import com.iso.logus.domain.team.domain.teamauth.TeamAuth;
import com.iso.logus.domain.team.domain.teamauth.TeamAuthRepository;
import com.iso.logus.domain.team.dto.TeamAuthDto;
import com.iso.logus.domain.team.exception.TeamAuthNameDuplicationException;
import com.iso.logus.domain.team.exception.TeamNotFoundException;
import com.iso.logus.domain.team.service.TeamAuthService;
import com.iso.logus.domain.team.service.TeamService;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TeamAuthServiceTest {
	
	@InjectMocks
	private TeamAuthService teamAuthService;
	
	@Mock
	private TeamAuthRepository teamAuthRepository;
	
	@Mock
	private TeamService teamService;
	
	private final static TeamAuthSampleData sampleData = new TeamAuthSampleData();
	
	private static Long teamMockId;
	private static Team team;
	
	private static TeamAuth allTrueAuth, allFalseAuth, sampleAuth;
	
	@BeforeAll
	public static void setUp() {
		teamMockId = 1L;
		team = sampleData.makeTeam(teamMockId);
		allTrueAuth = sampleData.returnAllTrueAuth(team);
		allFalseAuth = sampleData.returnAllFalseAuth(team);
		sampleAuth = sampleData.returnSampleAuth(team);
	}
	
	@Test
	@DisplayName("findList: team 식별자로 해당 팀의 모든 권한 조회")
	public void findListTest() {
		//given
		final List<TeamAuth> teamAuthList = new ArrayList<>();
		teamAuthList.add(allTrueAuth);
		teamAuthList.add(allFalseAuth);
		teamAuthList.add(sampleAuth);
		given(teamAuthRepository.findByTeamId(team.getId())).willReturn(teamAuthList);
		
		//when
		List<TeamAuthDto.Response> result = teamAuthService.findList(team.getId());
		
		//then
		assertNotNull(result);
		assertThat(result.size(), is(3));
	}
	
	@Test
	@DisplayName("findList: 존재하지 않는 team 식별자로 조회 후 TeamNotFoundException 반환 테스트")
	public void findListTest_NotExistTeamId() {
		//given
		given(teamAuthRepository.findByTeamId(team.getId())).willReturn(List.of());
		
		//when
		assertThrows(TeamNotFoundException.class, () -> {
			teamAuthService.findList(team.getId());
		});

		//then
		
	}
	
	@Test
	@DisplayName("createTeamAuth: 팀 권한 생성 테스트")
	public void createTeamAuthTest() {
		//given
		final TeamAuthDto.SaveRequest saveRequest = sampleData.saveRequestBuilder();
		given(teamService.findTeamById(teamMockId)).willReturn(team);
		given(teamAuthRepository.existsByTeamIdAndName(teamMockId, saveRequest.getName())).willReturn(false);
		
		//when
		teamAuthService.createTeamAuth(team.getId(), saveRequest);
		
		//then
	}
	
	@Test
	@DisplayName("createTeamAuth: 같은 이름의 팀 권한이 존재할 경우")
	public void createTeamAuthWithSameNameTest() {
		//given
		final TeamAuthDto.SaveRequest saveRequest = sampleData.saveRequestBuilder();
		given(teamAuthRepository.existsByTeamIdAndName(teamMockId, saveRequest.getName())).willReturn(true);
		
		//when
		assertThrows(TeamAuthNameDuplicationException.class, () -> {
			teamAuthService.createTeamAuth(team.getId(), saveRequest);
		});
		
		//then
	}
	
	@Test
	@DisplayName("changeTeamAuth: 팀 권한 사항 수정 테스트")
	public void changeTeamAuthTest() {
		//given
		final TeamAuthDto.UpdateRequest updateRequest = sampleData.updateRequestBuilder();
		final TeamAuth targetAuth = sampleData.returnSampleAuth(team);
		given(teamAuthRepository.findByTeamIdAndName(teamMockId, "special-class")).willReturn(Optional.of(targetAuth));
		
		//when
		teamAuthService.changeTeamAuth(team.getId(), "special-class", updateRequest);
		
		//then
		assertThat(targetAuth.getName(), is(updateRequest.getName()));
	}
	
	@Test
	@DisplayName("deleteTeamAuth: 팀 권한 삭제 테스트")
	public void deleteTeamAuth() {
		//given
		
		//when
		teamAuthService.deleteTeamAuth(teamMockId, "special-class");
		
		//then
		verify(teamAuthRepository, times(1)).deleteByTeamIdAndName(teamMockId, "special-class");
	}
}
