package com.iso.logus.domain.team.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.util.Pair;
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

@Service
@Transactional
@RequiredArgsConstructor
public class TeamAuthService {

	private final TeamAuthRepository teamAuthRepository;

	private final TeamSearchService teamSearchService;
	
	private static final TeamAuthBaseData baseData = new TeamAuthBaseData();
		
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
	
	@Transactional(readOnly = true)
	public TeamAuth findTeamAuthByTeamIdName(long teamId, String name) {
		return teamAuthRepository.findByTeamIdAndName(teamId, name).orElseThrow(TeamAuthNotFoundException::new);
	}

	public TeamAuth createTeamAuth(SaveRequest saveRequest) {
		if(isExistedTeamAuth(saveRequest.getTeamId(), saveRequest.getName()))
			throw new TeamAuthNameDuplicationException();
		Team team = teamSearchService.findTeamById(saveRequest.getTeamId());
		return teamAuthRepository.save(saveRequest.toEntity(team));
	}
	
	public Pair<TeamAuth,TeamAuth> setUpTeamAuth(Team team) {
		TeamAuth masterAuth = teamAuthRepository.save(baseData.createMasterAuth(team));
		TeamAuth defaultAuth = teamAuthRepository.save(baseData.createDefaultAuth(team));
		return Pair.of(masterAuth, defaultAuth);
	}

	public TeamAuth changeTeamAuth(UpdateRequest updateRequest) {
		TeamAuth teamAuth = findTeamAuthByTeamIdName(updateRequest.getTeamId(), updateRequest.getOriginName());
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
	
	@Transactional(readOnly = true)
	public TeamAuth findDefaultAuth(long teamId) {
		return teamAuthRepository.findByTeamIdAndTypeEqual(teamId, TeamAuthType.DEFAULT.getTeamAuthTypeValue()).orElseThrow(ServerErrorException::new);
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
			TeamAuth originDefaultAuth = findDefaultAuth(teamAuth.getTeam().getId());
			UpdateRequest originUpdateRequest = UpdateRequest.builder()
					.teamId(teamAuth.getTeam().getId())
					.originName(originDefaultAuth.getName())
					.changeName(originDefaultAuth.getName())
					.type(TeamAuthType.NONE)
					.masterAuth(originDefaultAuth.getMasterAuth())
					.memberControllAuth(originDefaultAuth.getMemberControllAuth())
					.activeAuth(originDefaultAuth.getActiveAuth())
					.build();
			originDefaultAuth.update(originUpdateRequest);
		}
	}

}
