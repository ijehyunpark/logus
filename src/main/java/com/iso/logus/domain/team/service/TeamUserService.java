package com.iso.logus.domain.team.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iso.logus.domain.team.domain.team.Team;
import com.iso.logus.domain.team.domain.teamauth.AuthType;
import com.iso.logus.domain.team.domain.teamauth.DetailAuth;
import com.iso.logus.domain.team.domain.teamauth.TeamAuth;
import com.iso.logus.domain.team.domain.teamauth.TeamAuthType;
import com.iso.logus.domain.team.domain.teamuser.TeamUser;
import com.iso.logus.domain.team.domain.teamuser.TeamUserRepository;
import com.iso.logus.domain.team.dto.TeamUserDto;
import com.iso.logus.domain.team.dto.TeamUserDto.MemberResponse;
import com.iso.logus.domain.team.dto.TeamUserDto.TeamResponse;
import com.iso.logus.domain.team.exception.TeamNotFoundException;
import com.iso.logus.domain.user.domain.User;
import com.iso.logus.domain.user.exception.UserNotFoundException;
import com.iso.logus.domain.user.service.UserService;
import com.iso.logus.global.exception.ServerErrorException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TeamUserService {

	private final TeamUserRepository teamUserRepository;
	private final TeamSearchService teamSearchService;
	private final UserService userService;
	private final TeamAuthService teamAuthService;
	
	public boolean compareAuth(TeamAuth teamAuth, AuthType type) {
		try {
			/* TeamAuth의 DetailAuth에 대한 class getter 가져오기 */
			String getClassMethodName = "get" + type.getAuthClassName();
			Method getClassMethod = teamAuth.getClass().getMethod(getClassMethodName);
			DetailAuth detailAuth = (DetailAuth) getClassMethod.invoke(teamAuth);
			
			/* DetailAuth의 특정 필드의 getter 가져오기 */
			String getFieldMethodName = "is" + type.getAuthType().substring(0,1).toUpperCase() + type.getAuthType().substring(1);
			Method getFieldMethod = detailAuth.getClass().getMethod(getFieldMethodName);
			return (boolean) getFieldMethod.invoke(detailAuth);
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException | NoSuchMethodException | InvocationTargetException e) {
			throw new ServerErrorException();
		}
	}
	
	@Transactional(readOnly = true)
	public boolean isUserHasAuth(long teamId, String uid, AuthType authType) {
		TeamUser teamUser = findTeamUserByTeamIdAndUserId(teamId, uid);
		return compareAuth(teamUser.getTeamAuth(), authType);
	}
	
	public TeamUser findMemberHasMasterAuth(long teamId) {
		List<TeamUser> tu = teamUserRepository.findByTeamAuthType(teamId, TeamAuthType.MASTER.getTeamAuthTypeValue());
		if(tu.size() == 1)
			return tu.get(0);
		
		if(tu.size() == 0)
			throw new TeamNotFoundException();
		else
			throw new ServerErrorException();
		
	}
	
	@Transactional(readOnly = true)
	public boolean isUserHasMasterAuth(long teamId, String uid) {
		TeamUser teamUser = findMemberHasMasterAuth(teamId);
		log.info("" + uid + " / " + teamUser.getUser().getUid());
		return teamUser.getUser().getUid().equals(uid);
	}
	
	@Transactional(readOnly = true)
	public List<TeamUser> findAllTeamUserByTeam(long teamId) {
		if(!teamSearchService.isExistsTeamById(teamId))
			throw new TeamNotFoundException();
		List<TeamUser> result = teamUserRepository.findAllByTeamId(teamId);
		return result;
	}
	
	@Transactional(readOnly = true)
	public List<MemberResponse> findAllMemberByTeam(long teamId) {
		List<TeamUser> teamUserList = findAllTeamUserByTeam(teamId);
		List<MemberResponse> dtoList = new ArrayList<>();
		for(TeamUser teamUser : teamUserList) 
			dtoList.add(new MemberResponse(teamUser));
		return dtoList;
	}
	
	@Transactional(readOnly = true)
	public List<TeamUser> findAllTeamUserByUser(String uid) {
		if(!userService.isExistedUid(uid))
			throw new UserNotFoundException();
		List<TeamUser> result = teamUserRepository.findAllByUserUid(uid);
		return result;
	}
	
	@Transactional(readOnly = true)
	public List<TeamResponse> findAllTeamByUser(String uid) {
		List<TeamUser> teamUserList = findAllTeamUserByUser(uid);
		List<TeamResponse> dtoList = new ArrayList<>();
		for(TeamUser teamUser : teamUserList) 
			dtoList.add(new TeamResponse(teamUser));
		return dtoList;
	}
	
	@Transactional(readOnly = true)
	public TeamUser findTeamUserByTeamIdAndUserId(long teamId, String uid) {
		return teamUserRepository.findByTeamIdAndUserUid(teamId, uid).orElseThrow(UserNotFoundException::new);
	}
	
	public TeamUser joinNewMember(TeamUserDto.JoinRequest joinRequest) {
		Team team = teamSearchService.findTeamById(joinRequest.getTeamId());
		User user = userService.findUserByUid(joinRequest.getUid());
		TeamAuth teamAuth = teamAuthService.findDefaultAuth(joinRequest.getTeamId());
		return teamUserRepository.save(joinRequest.toEntity(team, user, teamAuth));
	}
	
	public void quitMember(TeamUserDto.QuitRequest quitRequest) {
		teamUserRepository.deleteByTeamIdAndUserUid(quitRequest.getTeamId(), quitRequest.getUid());
	}
	
	public TeamUser changeAuth(TeamUserDto.ChangeAuthRequest changeAuthRequest) {
		TeamUser teamUser = findTeamUserByTeamIdAndUserId(changeAuthRequest.getTeamId(), changeAuthRequest.getUid());
		TeamAuth teamAuth = teamAuthService.findTeamAuthByTeamIdName(changeAuthRequest.getTeamId(), changeAuthRequest.getTeamAuthName());
		teamUser.updateTeamAuth(teamAuth);
		return teamUser;
	}
	
	public TeamUser changeCustomName(TeamUserDto.ChangeCustomNameRequest customNameRequest) {
		TeamUser teamUser = findTeamUserByTeamIdAndUserId(customNameRequest.getTeamId(), customNameRequest.getUid());
		teamUser.updateCustomName(customNameRequest);
		return teamUser;
	}
}
