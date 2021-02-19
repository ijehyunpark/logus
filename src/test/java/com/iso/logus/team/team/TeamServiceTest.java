package com.iso.logus.team.team;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
import com.iso.logus.domain.team.dto.TeamDto;
import com.iso.logus.domain.team.service.TeamAuthService;
import com.iso.logus.domain.team.service.TeamSearchService;
import com.iso.logus.domain.team.service.TeamService;
import com.iso.logus.team.TeamTestSampleData;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {
	
	@InjectMocks
	private TeamService teamService;
	
	@Mock
	private TeamSearchService teamSearchService;
	
	@Mock
	private TeamRepository teamRepository;
	
	@Mock
	private TeamAuthService teamAuthService;
	
	private static Team team;
	
	private static final TeamTestSampleData sampleData = new TeamTestSampleData();
	
	@BeforeAll
	public static void setUp() {
		team = sampleData.makeTeam();
	}
	
	@Test
	@DisplayName("createTeam: 팀 생성 테스트")
	public void createTeamTest() {
		//given
		TeamDto.CreateRequest createRequest = sampleData.makeTeamRequest();
		Team testTeam = sampleData.makeTeam(1L);
		given(teamRepository.save(any(Team.class))).willReturn(testTeam);
		
		//when
		teamService.createTeam(createRequest);
		
		//then
		verify(teamRepository, atLeastOnce()).save(any(Team.class));
	}
	
	@Test
	@DisplayName("changeTeam: 팀 설정 변경 테스트")
	public void changeTeamTest() {
		//given
		TeamDto.UpdateRequest updateRequest = TeamDto.UpdateRequest.builder()
				.name("changed team name")
				.descript("changed descript")
				.build();
		given(teamSearchService.findTeamById(anyLong())).willReturn(team);
		
		//when
		Team result = teamService.updateTeam(anyLong(), updateRequest);
		
		//then
		assertNotNull(result);
		assertEquals(team, result);
		assertThat(result.getName(), is(updateRequest.getName()));
		assertThat(result.getDescript(), is(updateRequest.getDescript()));
		
	}
	
	@Test
	@DisplayName("changeTeam: 팀 설명 변경 테스트, 빈 값은 원래 값 유지")
	public void changeTeamDescriptTest() {
		//given
		TeamDto.UpdateRequest updateRequest = TeamDto.UpdateRequest.builder()
				.descript("changed descript")
				.build();
		String originName = team.getName();
		given(teamSearchService.findTeamById(anyLong())).willReturn(team);
		
		//when
		Team result = teamService.updateTeam(anyLong(), updateRequest);
		
		//then
		assertNotNull(result);
		assertEquals(team, result);
		assertThat(result.getName(), is(originName));
		assertThat(result.getDescript(), is(updateRequest.getDescript()));
		
	}
	
	@Test
	@DisplayName("deleteTeam: 팀 삭제 테스트")
	public void deleteTeamTest() {
		//given
		given(teamSearchService.findTeamById(anyLong())).willReturn(team);
		
		//when
		teamService.deleteTeam(anyLong());
		
		//then
		verify(teamRepository, times(1)).delete(team);
	}
}
