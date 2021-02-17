package com.iso.logus.team.teamuser;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
import com.iso.logus.domain.team.domain.teamuser.TeamUser;
import com.iso.logus.domain.team.domain.teamuser.TeamUserRepository;
import com.iso.logus.domain.team.dto.TeamUserDto;
import com.iso.logus.domain.team.dto.TeamUserDto.ChangeAuthRequest;
import com.iso.logus.domain.team.dto.TeamUserDto.ChangeCustomNameRequest;
import com.iso.logus.domain.team.dto.TeamUserDto.JoinRequest;
import com.iso.logus.domain.team.dto.TeamUserDto.MemberResponse;
import com.iso.logus.domain.team.dto.TeamUserDto.QuitRequest;
import com.iso.logus.domain.team.dto.TeamUserDto.TeamResponse;
import com.iso.logus.domain.team.exception.TeamNotFoundException;
import com.iso.logus.domain.team.service.TeamAuthBaseData;
import com.iso.logus.domain.team.service.TeamAuthService;
import com.iso.logus.domain.team.service.TeamService;
import com.iso.logus.domain.team.service.TeamUserService;
import com.iso.logus.domain.user.domain.User;
import com.iso.logus.domain.user.dto.UserDto;
import com.iso.logus.domain.user.exception.UserNotFoundException;
import com.iso.logus.domain.user.service.UserService;
import com.iso.logus.team.TeamTestSampleData;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TeamUserServiceTest {
	
	@InjectMocks
	private TeamUserService teamUserService;
	
	@Mock
	private TeamUserRepository teamUserRepository;
	@Mock
	private TeamService teamService;
	@Mock
	private UserService userService;
	@Mock
	private TeamAuthService teamAuthService;
	
	private final static TeamTestSampleData sampleData = new TeamTestSampleData();
	private final static TeamAuthBaseData authBaseData = new TeamAuthBaseData();
	
	/*
	 * masterUser: team의 최종 권한을 가진 유저의 예제
	 * user{1, 2}: team의 일반 유저의 예제
	 * user{3}: team에 소속되지 않은 유저
	 */
	private static Team team;
	private static TeamAuth masterAuth, defaultAuth, customAuth;
	private static User masterUser, user1, user2, user3;
	private static TeamUser masterTeamUser, teamUser1, teamUser2;
	
	@BeforeAll
	public static void setUp() {
		team = sampleData.makeTeam(1L);
		masterAuth = authBaseData.createMasterAuth(team);
		defaultAuth = authBaseData.createDefaultAuth(team);
		customAuth = sampleData.returnSampleAuth(team, "custom-rank");
		masterUser = userBuilder(0);
		user1 = userBuilder(1);
		user2 = userBuilder(2);
		user3 = userBuilder(3);
		
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
	@DisplayName("findAllMemberByTeamTest: 특정 팀에 소속된 모든 유저 조회")
	public void findAllMemberByTeamTest() {
		//given
		List<TeamUser> teamUserList = new ArrayList<>();
		teamUserList.add(masterTeamUser);
		teamUserList.add(teamUser1);
		teamUserList.add(teamUser2);
		given(teamUserRepository.findAllByTeamId(anyLong())).willReturn(teamUserList);
		
		//when
		List<MemberResponse> result = teamUserService.findAllMemberByTeam(anyLong());
		
		//then
		assertNotNull(result);
		assertThat(result.size(), is(3));
	}
	
	@Test
	@DisplayName("findAllMemberByTeamTest: 존재하지 않은 팀으로 조회 요청")
	public void findAllMemberByTeamTest_TeamNotFound() {
		//given
		given(teamUserRepository.findAllByTeamId(anyLong())).willReturn(List.of());
		
		//when
		assertThrows(TeamNotFoundException.class, () -> {
			teamUserService.findAllMemberByTeam(anyLong());	
		});
		
		//then
		
	}
	
	@Test
	@DisplayName("findAllTeamByUserTest: 특정 유저가 참가한 모든 팀 조회")
	public void findAllTeamByUserTest() {
		//given
		List<TeamUser> teamUserList = new ArrayList<>();
		teamUserList.add(teamUser1);
		given(teamUserRepository.findAllByUserUid(anyString())).willReturn(teamUserList);
		
		//when
		List<TeamResponse> dtoList = teamUserService.findAllTeamByUser(anyString());
		
		//then
		assertNotNull(dtoList);
		assertThat(dtoList.size(), is(1));
	}
	
	@Test
	@DisplayName("findAllTeamByUserTest: 존재하지 않은 유저로 조회 요청")
	public void findAllTeamByUserTest_UserNotFound() {
		//given
		given(teamUserRepository.findAllByUserUid(anyString())).willReturn(List.of());
		
		//when
		assertThrows(UserNotFoundException.class, () -> {
			teamUserService.findAllTeamByUser(anyString());
		});
		
		//then
	}
	
	@Test
	@DisplayName("joinNewMember: 유저 참가")
	public void joinNewMemberTest() {
		//given
		JoinRequest joinRequest = JoinRequest.builder()
				.teamId(team.getId())
				.uid(user3.getUid())
				.build();
		given(teamService.findTeamById(team.getId())).willReturn(team);
		given(userService.findUserByUid(user3.getUid())).willReturn(user3);
		given(teamAuthService.findDefaultAuth(team.getId())).willReturn(defaultAuth);
		
		//when
		teamUserService.joinNewMember(joinRequest);
		
		//then
		verify(teamUserRepository, times(1)).save(any(TeamUser.class));
	}
	
	@Test
	@DisplayName("quitMember: 유저 탈퇴")
	public void quitMemberTest() {
		//given
		QuitRequest quitRequest = QuitRequest.builder()
				.teamId(team.getId())
				.uid(user2.getUid())
				.build();
		
		//when
		teamUserService.quitMember(quitRequest);
		
		//then
		verify(teamUserRepository, times(1)).deleteByTeamIdAndUserUid(team.getId(), user2.getUid());
	}
	
	@Test
	@DisplayName("changeAuth: 팀에서의 유저 권한 변경")
	public void changeAuthTest() {
		//given
		ChangeAuthRequest chageAuthRequest = ChangeAuthRequest.builder()
				.teamId(team.getId())
				.uid(user1.getUid())
				.teamAuthName("custom-rank")
				.build();
		given(teamUserRepository.findByTeamIdAndUserUid(team.getId(), user1.getUid())).willReturn(Optional.of(teamUser1));
		given(teamAuthService.findTeamAuthByTeamIdName(team.getId(), "custom-rank")).willReturn(customAuth);
		
		//when
		TeamUser result = teamUserService.changeAuth(chageAuthRequest);
		
		//then
		verify(teamUserRepository, times(1)).findByTeamIdAndUserUid(team.getId(), user1.getUid());
		assertThat(result.getTeamAuth().getName(), is("custom-rank"));
		assertEquals(teamUser1, result);
	}
	
	@Test
	@DisplayName("changeCustomName: 팀에서의 이름 변경")
	public void changeCustomName() {
		//given
		ChangeCustomNameRequest changeCustomNameRequest = ChangeCustomNameRequest.builder()
				.teamId(team.getId())
				.uid(user1.getUid())
				.customName("changed")
				.build();
		given(teamUserRepository.findByTeamIdAndUserUid(team.getId(), user1.getUid())).willReturn(Optional.of(teamUser1));
		
		//when
		TeamUser result = teamUserService.changeCustomName(changeCustomNameRequest);
		
		//then
		verify(teamUserRepository, times(1)).findByTeamIdAndUserUid(team.getId(), user1.getUid());
		assertThat(result.getCustomName(), is("changed"));
		assertEquals(teamUser1, result);
	}
	
	public static User userBuilder(long idNum) {
		return UserDto.SignUpRequest.builder()
						.uid("user" + idNum)
						.name("user" + idNum)
						.password("password")
						.build()
						.toEntity();
	}
}
