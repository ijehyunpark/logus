package com.iso.logus.domain.team.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iso.logus.domain.team.domain.team.Team;
import com.iso.logus.domain.team.domain.teamauth.TeamAuth;
import com.iso.logus.domain.team.domain.teamauth.TeamAuthRepository;
import com.iso.logus.domain.team.domain.teamauth.TeamAuthType;
import com.iso.logus.domain.team.dto.TeamAuthDto;
import com.iso.logus.domain.team.dto.TeamAuthDto.SaveRequest;
import com.iso.logus.domain.team.dto.TeamAuthDto.UpdateRequest;
import com.iso.logus.domain.team.exception.TeamAuthMasterAuthException;
import com.iso.logus.domain.team.exception.TeamAuthNameDuplicationException;
import com.iso.logus.domain.team.exception.TeamAuthNotFoundException;
import com.iso.logus.domain.team.exception.TeamAuthSpecialTypeDeleteException;
import com.iso.logus.domain.team.exception.TeamNotFoundException;
import com.iso.logus.global.exception.ServerErrorException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamAuthService {

	private final TeamAuthRepository teamAuthRepository;

	private final TeamService teamService;

	@Transactional(readOnly = true)
	public List<TeamAuth> findTeamAuthList(long teamId) {
		List<TeamAuth> teamAuthList = teamAuthRepository.findByTeamId(teamId);
		if(teamAuthList.size() == 0)
			throw new TeamNotFoundException();
		return teamAuthList;
	}
	
	@Transactional(readOnly = true)
	public List<TeamAuthDto.Response> findList(long teamId) {
		List<TeamAuthDto.Response> dtoList = new ArrayList<>();
		List<TeamAuth> teamAuthList = findTeamAuthList(teamId);
		for(TeamAuth teamAuth : teamAuthList)
			dtoList.add(new TeamAuthDto.Response(teamAuth));
		return dtoList;
	}
	
	@Transactional(readOnly = true)
	public boolean isExistedTeamAuth(long teamId, String name) {
		return teamAuthRepository.existsByTeamIdAndName(teamId, name);
	}

	public TeamAuth createTeamAuth(long teamId, SaveRequest saveRequest) {
		if(isExistedTeamAuth(teamId, saveRequest.getName()))
			throw new TeamAuthNameDuplicationException();
		Team team = teamService.findTeamById(teamId);
		return teamAuthRepository.save(saveRequest.toEntity(team));
	}

	public TeamAuth changeTeamAuth(long teamId, String name, UpdateRequest updateRequest) {
		TeamAuth teamAuth = teamAuthRepository.findByTeamIdAndName(teamId, name).orElseThrow(TeamAuthNotFoundException::new);
		if(masterAuthCheck(teamAuth, updateRequest))
			throw new TeamAuthMasterAuthException();
		defaultAuthCheck(teamAuth, updateRequest);
		teamAuth.update(updateRequest);
		return teamAuth;
	}

	public void deleteTeamAuth(long teamId, String name) {
		TeamAuth teamAuth = teamAuthRepository.findByTeamIdAndName(teamId, name).orElseThrow(TeamAuthNotFoundException::new);
		if(TeamAuthType.values()[teamAuth.getType()] != TeamAuthType.NONE)
			throw new TeamAuthSpecialTypeDeleteException();
		teamAuthRepository.delete(teamAuth);
	}
	
	public boolean masterAuthCheck(TeamAuth teamAuth, UpdateRequest updateRequest) {
		if(TeamAuthType.values()[teamAuth.getType()] == TeamAuthType.MASTER) {
			//권한 체크
			if(!updateRequest.getMasterAuth().checkAllTrue() || 
				!updateRequest.getMemberControllAuth().checkAllTrue() || 
				!updateRequest.getActiveAuth().checkAllTrue())
				return true;
			//분류 체크
			if(updateRequest.getType() != TeamAuthType.MASTER)
				return true;
		}
		return false;
	}
	
	public void defaultAuthCheck(TeamAuth teamAuth, UpdateRequest updateRequest) {
		if(TeamAuthType.values()[teamAuth.getType()] == TeamAuthType.NONE &&
			updateRequest.getType() == TeamAuthType.DEFAULT) {
			TeamAuth originDefaultAuth = teamAuthRepository.findByTeamIdAndTypeEqual(teamAuth.getTeam().getId(), TeamAuthType.DEFAULT.getTeamAuthTypeValue()).orElseThrow(ServerErrorException::new);
			UpdateRequest originUpdateRequest = UpdateRequest.builder()
					.name(originDefaultAuth.getName())
					.type(TeamAuthType.NONE)
					.masterAuth(originDefaultAuth.getMasterAuth())
					.memberControllAuth(originDefaultAuth.getMemberControllAuth())
					.activeAuth(originDefaultAuth.getActiveAuth())
					.build();
			originDefaultAuth.update(originUpdateRequest);
		}
	}
}
