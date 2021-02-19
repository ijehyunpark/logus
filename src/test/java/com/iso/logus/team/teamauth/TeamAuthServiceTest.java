package com.iso.logus.team.teamauth;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
import com.iso.logus.domain.team.domain.teamauth.TeamAuth;
import com.iso.logus.domain.team.domain.teamauth.TeamAuthRepository;
import com.iso.logus.domain.team.domain.teamauth.TeamAuthType;
import com.iso.logus.domain.team.dto.TeamAuthDto;
import com.iso.logus.domain.team.exception.TeamAuthMasterAuthException;
import com.iso.logus.domain.team.exception.TeamAuthNameDuplicationException;
import com.iso.logus.domain.team.exception.TeamAuthSpecialTypeDeleteException;
import com.iso.logus.domain.team.exception.TeamNotFoundException;
import com.iso.logus.domain.team.service.TeamAuthBaseData;
import com.iso.logus.domain.team.service.TeamAuthService;
import com.iso.logus.domain.team.service.TeamSearchService;
import com.iso.logus.team.TeamTestSampleData;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TeamAuthServiceTest {
	
	@InjectMocks
	private TeamAuthService teamAuthService;
	
	@Mock
	private TeamAuthRepository teamAuthRepository;
	
	@Mock
	private TeamSearchService teamSearchService;
	
	private final static TeamTestSampleData sampleData = new TeamTestSampleData();
	private final static TeamAuthBaseData baseData = new TeamAuthBaseData();
	
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
		given(teamAuthRepository.findByTeamId(anyLong())).willReturn(teamAuthList);
		
		//when
		List<TeamAuthDto.Response> result = teamAuthService.findList(anyLong());
		
		//then
		assertNotNull(result);
		assertThat(result.size(), is(3));
	}
	
	@Test
	@DisplayName("findList: 존재하지 않는 team 식별자로 조회 후 TeamNotFoundException 반환 테스트")
	public void findListTest_NotExistTeamId() {
		//given
		given(teamAuthRepository.findByTeamId(anyLong())).willReturn(List.of());
		
		//when
		assertThrows(TeamNotFoundException.class, () -> {
			teamAuthService.findList(anyLong());
		});

		//then
		
	}
	
	@Test
	@DisplayName("createTeamAuth: 팀 권한 생성 테스트")
	public void createTeamAuthTest() {
		//given
		final TeamAuthDto.SaveRequest saveRequest = sampleData.saveRequestBuilder(teamMockId);
		given(teamSearchService.findTeamById(teamMockId)).willReturn(team);
		given(teamAuthRepository.existsByTeamIdAndName(teamMockId, saveRequest.getName())).willReturn(false);
		
		//when
		teamAuthService.createTeamAuth(saveRequest);
		
		//then
	}
	
	@Test
	@DisplayName("createTeamAuth: 같은 이름의 팀 권한이 존재할 경우")
	public void createTeamAuthWithSameNameTest() {
		//given
		final TeamAuthDto.SaveRequest saveRequest = sampleData.saveRequestBuilder(teamMockId);
		given(teamAuthRepository.existsByTeamIdAndName(teamMockId, saveRequest.getName())).willReturn(true);
		
		//when
		assertThrows(TeamAuthNameDuplicationException.class, () -> {
			teamAuthService.createTeamAuth(saveRequest);
		});
		
		//then
	}
	
	@Test
	@DisplayName("changeTeamAuth: 팀 권한 사항 수정 테스트")
	public void changeTeamAuthTest() {
		//given
		final TeamAuthDto.UpdateRequest updateRequest = sampleData.updateRequestBuilder(teamMockId, "special-class", "changed", TeamAuthType.NONE);
		final TeamAuth targetAuth = sampleData.returnSampleAuth(team);
		given(teamAuthRepository.findByTeamIdAndName(teamMockId, "special-class")).willReturn(Optional.of(targetAuth));
		
		//when
		teamAuthService.changeTeamAuth(updateRequest);
		
		//then
		assertThat(targetAuth.getName(), is(updateRequest.getChangeName()));
	}
	
	@Test
	@DisplayName("changeTeamAuth: 마스터 권한의 모든 권한은 항상 참이어야 함")
	public void changeTeamAuthTest_MasterAuth1() {
		//given
		final TeamAuthDto.UpdateRequest updateRequest = sampleData.updateRequestBuilder(team.getId(), "Team Master", "Team Master", TeamAuthType.MASTER);
		final TeamAuth masterAuth = baseData.createMasterAuth(team);
		given(teamAuthRepository.findByTeamIdAndName(teamMockId, "Team Master")).willReturn(Optional.of(masterAuth));
		
		//when
		assertThrows(TeamAuthMasterAuthException.class, () -> {
			teamAuthService.changeTeamAuth(updateRequest);
		});
		
		//then
		
	}
	
	@Test
	@DisplayName("changeTeamAuth: 마스터 권한의 분류를 수정할 수 없음")
	public void changeTeamAuthTest_MasterAuth2() {
		//given
		
		final TeamAuthDto.UpdateRequest updateRequest = sampleData.updateMasterBuilder(team.getId(), "Team Master", "Team Master", TeamAuthType.DEFAULT);
		final TeamAuth masterAuth = baseData.createMasterAuth(team);
		given(teamAuthRepository.findByTeamIdAndName(teamMockId, "Team Master")).willReturn(Optional.of(masterAuth));
		
		//when
		assertThrows(TeamAuthMasterAuthException.class, () -> {
			teamAuthService.changeTeamAuth(updateRequest);
		});
		
		//then
		
	}
	
	@Test
	@DisplayName("changeTeamAuth: 마스터 권한의 이름은 수정될 수 있음")
	public void changeTeamAuthTest_MasterAuth3() {
		//given
		final TeamAuthDto.UpdateRequest updateRequest = sampleData.updateMasterBuilder(0, "anyString", "changed", TeamAuthType.MASTER);
		final TeamAuth masterAuth = baseData.createMasterAuth(team);
		given(teamAuthRepository.findByTeamIdAndName(anyLong(), anyString())).willReturn(Optional.of(masterAuth));
		
		//when
		teamAuthService.changeTeamAuth(updateRequest);
		
		//then
		assertThat(masterAuth.getName(), is("changed"));
		
	}
	
	@Test
	@DisplayName("changeTeamAuth: 일반 분류의 권한이 기본 분류로 수정되면 기존의 기본 분류 권한은 일반으로 분류됨")
	public void changeTeamAuthTest_DefaultAuth1() {
		//given
		final TeamAuth orginDefaultAuth = baseData.createDefaultAuth(team);
		final TeamAuth newDefaultAuth = sampleData.returnSampleAuth(team);
		final TeamAuthDto.UpdateRequest updateRequest = sampleData.updateRequestBuilder(team.getId(), newDefaultAuth.getName(), "New Default Auth", TeamAuthType.DEFAULT);
		given(teamAuthRepository.findByTeamIdAndTypeEqual(team.getId(), TeamAuthType.DEFAULT.getTeamAuthTypeValue()))
			.willReturn(Optional.of(orginDefaultAuth));
		given(teamAuthRepository.findByTeamIdAndName(team.getId(), newDefaultAuth.getName())).willReturn(Optional.of(newDefaultAuth));
		
		//when
		teamAuthService.changeTeamAuth(updateRequest);
		
		//then
		assertThat(TeamAuthType.values()[newDefaultAuth.getType()], is(TeamAuthType.DEFAULT));
		assertThat(TeamAuthType.values()[orginDefaultAuth.getType()], is(TeamAuthType.NONE));
		
	}
	
	@Test
	@DisplayName("deleteTeamAuth: 팀 권한 삭제 테스트")
	public void deleteTeamAuth() {
		//given
		final TeamAuth sampleAuth = sampleData.returnSampleAuth(team);
		given(teamAuthRepository.findByTeamIdAndName(anyLong(), anyString())).willReturn(Optional.of(sampleAuth));
		
		//when
		teamAuthService.deleteTeamAuth(anyLong(), anyString());
		
		//then
		verify(teamAuthRepository, times(1)).delete(sampleAuth);
	}
	
	@Test
	@DisplayName("deleteTeamAuth: 특수 분류가 된 권한은 삭제 될 수 없음")
	public void deleteTeamAuth_Type() {
		//given
		final TeamAuth masterAuth = baseData.createMasterAuth(team);
		given(teamAuthRepository.findByTeamIdAndName(anyLong(), anyString())).willReturn(Optional.of(masterAuth));
		
		//when
		assertThrows(TeamAuthSpecialTypeDeleteException.class, () -> {
			teamAuthService.deleteTeamAuth(anyLong(), anyString());
		});
		
		//then
	
	}
}
