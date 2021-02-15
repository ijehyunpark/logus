package com.iso.logus.team.team;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
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
import com.iso.logus.domain.team.dto.TeamDto;
import com.iso.logus.domain.team.exception.TeamNotFoundException;
import com.iso.logus.domain.team.service.TeamService;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

	@InjectMocks
	private TeamService teamService;
	
	@Mock
	private TeamRepository teamRepository;
	
	private static Team team1, team2;
	
	@BeforeAll
	public static void setUp() {
		team1 = TeamDto.CreateRequest.builder()
					.name("testTeam1")
					.descript("sample1")
					.build()
					.toEntity();
		team1.setIdForTest(1L);
		team2 = TeamDto.CreateRequest.builder()
					.name("testTeam2")
					.descript("sample2")
					.build()
					.toEntity();
		team2.setIdForTest(2L);
	}
	
	@Test
	@DisplayName("changeResponseDto: Team Entity -> ResponseDto 변환 테스트")
	public void changeResponseDtoTest() {
		//given
		final List<Team> teamList = teamListBuilder();
		//when
		final List<TeamDto.Response> dtoList = teamService.changeResponseDto(teamList);
		
		//then
		assertNotNull(dtoList);
		assertThat(dtoList.size(), is(2));
		assertThat(dtoList.get(0).getName(), is("testTeam1"));
		assertThat(dtoList.get(0).getDescript(), is("sample1"));
	}
	
	@Test
	@DisplayName("findTeamList: Team Entity 반환 테스트")
	public void findTeamListTest() {
		//given
		final List<Team> teamList = teamListBuilder();
		given(teamRepository.findAll()).willReturn(teamList);
		
		//when
		final List<Team> findTeamList = teamService.findTeamList();
		
		//then
		assertNotNull(findTeamList);
		assertEquals(2, findTeamList.size());
		assertThat(findTeamList.get(0).getName(), is("testTeam1"));
		assertThat(findTeamList.get(0).getDescript(), is("sample1"));
	}
	
	@Test
	@DisplayName("findList: 모든 team 리스트 반환 테스트")
	public void findListTest() {
		//given
		final List<Team> teamList = teamListBuilder();
		given(teamRepository.findAll()).willReturn(teamList);
		
		//when
		final List<TeamDto.Response> findList = teamService.findList();
		
		//then
		assertNotNull(findList);
		assertEquals(2, findList.size());
		assertThat(findList.get(0).getName(), is("testTeam1"));
		assertThat(findList.get(0).getDescript(), is("sample1"));
	}
	
	@Test
	@DisplayName("findTeamByName: Team Entity 반환 테스트")
	public void findTeamByNameTest() {
		//given
		final List<Team> teamList = sameNameTeamListBuilder();
		given(teamRepository.findAllByName("testTeam1")).willReturn(teamList);
		
		//when
		final List<Team> findTeamList = teamService.findTeamByName("testTeam1");
		
		//then
		assertNotNull(findTeamList);
		assertEquals(2, findTeamList.size());
		assertThat(findTeamList.get(0).getName(), is("testTeam1"));
		assertThat(findTeamList.get(1).getName(), is("testTeam1"));
	}
	
	@Test
	@DisplayName("findByName: 특정 이름을 가진 모든 team 리스트 반환 테스트")
	public void findByNameTest() {
		//given
		final List<Team> teamList = sameNameTeamListBuilder();
		given(teamRepository.findAllByName("testTeam1")).willReturn(teamList);
		
		//when
		final List<TeamDto.Response> findList = teamService.findByName("testTeam1");
		
		//then
		assertNotNull(findList);
		assertEquals(2, findList.size());
		assertThat(findList.get(0).getName(), is("testTeam1"));
		assertThat(findList.get(1).getName(), is("testTeam1"));
	}
	
	@Test
	@DisplayName("findTeamById: 특정 id를 가진 team 반환 테스트_성공")
	public void findTeamByIdTest_success() {
		//given
		given(teamRepository.findById(anyLong())).willReturn(Optional.of(team1));
		
		//when
		final Team team = teamService.findTeamById(anyLong());
		
		//then
		assertNotNull(team);
		assertThat(team.getName(), is("testTeam1"));
		assertThat(team.getDescript(), is("sample1"));
	}
	
	@Test
	@DisplayName("findTeamById: 특정 id를 가진 team 반환 테스트_실패패")
	public void findTeamByIdTest_fail() {
		//given
		given(teamRepository.findById(anyLong())).willReturn(Optional.empty());
		
		//when
		assertThrows(TeamNotFoundException.class, () ->{
			teamService.findTeamById(anyLong());
		});
		
		//then
	}
	
	@Test
	@DisplayName("createTeam: 팀 생성 테스트")
	public void createTeamTest() {
		//given
		TeamDto.CreateRequest createRequest = TeamDto.CreateRequest.builder()
				.name("testTeam")
				.descript("sample")
				.build();
		
		//when
		teamService.createTeam(createRequest);
		
		//then
		
	}
	
	@Test
	@DisplayName("changeTeamDescript: 팀 설명 변경 테스트")
	public void changeTeamDescriptTest() {
		//given
		TeamDto.ChangeDescriptRequest changeDescriptRequest = TeamDto.ChangeDescriptRequest.builder()
				.descript("changed descript")
				.build();
		given(teamRepository.findById(anyLong())).willReturn(Optional.of(team1));
		
		//when
		teamService.changeTeamDescript(anyLong(), changeDescriptRequest);
		
		//then
		assertThat(team1.getDescript(), is(changeDescriptRequest.getDescript()));
		
	}
	
	@Test
	@DisplayName("deleteTeam: 팀 삭제 테스트")
	public void deleteTeamTest() {
		//given
		given(teamRepository.findById(anyLong())).willReturn(Optional.of(team1));
		
		//when
		teamService.deleteTeam(anyLong());
		
		//then
		verify(teamRepository, times(1)).delete(team1);
	}
	
	public List<Team> teamListBuilder() {
		List<Team> teamList = new ArrayList<>();
		teamList.add(team1);
		teamList.add(team2);
		return teamList;
	}
	
	public List<Team> sameNameTeamListBuilder() {
		List<Team> teamList = new ArrayList<>();
		teamList.add(team1);
		
		Team team3 = TeamDto.CreateRequest.builder()
				.name("testTeam1")
				.descript("sample3")
				.build()
				.toEntity();
		team3.setIdForTest(3L);
		teamList.add(team3);
		return teamList;
	}
}
