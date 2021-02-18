package com.iso.logus.domain.team.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iso.logus.domain.team.domain.team.Team;
import com.iso.logus.domain.team.domain.teamauth.TeamAuth;
import com.iso.logus.domain.team.domain.teamuser.TeamUser;
import com.iso.logus.domain.team.domain.teamuser.TeamUserRepository;
import com.iso.logus.domain.team.dto.TeamUserDto;
import com.iso.logus.domain.team.dto.TeamUserDto.MemberResponse;
import com.iso.logus.domain.team.dto.TeamUserDto.TeamResponse;
import com.iso.logus.domain.team.exception.TeamNotFoundException;
import com.iso.logus.domain.user.domain.User;
import com.iso.logus.domain.user.exception.UserNotFoundException;
import com.iso.logus.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamUserService {

	private final TeamUserRepository teamUserRepository;
	private final TeamSearchService teamSearchService;
	private final UserService userService;
	private final TeamAuthService teamAuthService;
	
	@Transactional(readOnly = true)
	public List<TeamUser> findAllTeamUserByTeam(long team_id) {
		List<TeamUser> result = teamUserRepository.findAllByTeamId(team_id);
		if(result.isEmpty())
			throw new TeamNotFoundException();
		return result;
	}
	
	@Transactional(readOnly = true)
	public List<MemberResponse> findAllMemberByTeam(long team_id) {
		List<TeamUser> teamUserList = findAllTeamUserByTeam(team_id);
		List<MemberResponse> dtoList = new ArrayList<>();
		for(TeamUser teamUser : teamUserList) 
			dtoList.add(new MemberResponse(teamUser));
		return dtoList;
	}
	
	@Transactional(readOnly = true)
	public List<TeamUser> findAllTeamUserByUser(String uid) {
		List<TeamUser> result = teamUserRepository.findAllByUserUid(uid);
		if(result.isEmpty())
			throw new UserNotFoundException();
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
	public TeamUser findTeamUserByTeamIdAndUserId(long team_id, String uid) {
		return teamUserRepository.findByTeamIdAndUserUid(team_id, uid).orElseThrow(UserNotFoundException::new);
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
