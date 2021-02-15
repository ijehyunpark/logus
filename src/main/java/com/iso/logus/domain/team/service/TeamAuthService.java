package com.iso.logus.domain.team.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iso.logus.domain.team.domain.team.Team;
import com.iso.logus.domain.team.domain.teamauth.TeamAuth;
import com.iso.logus.domain.team.domain.teamauth.TeamAuthRepository;
import com.iso.logus.domain.team.dto.TeamAuthDto;
import com.iso.logus.domain.team.dto.TeamAuthDto.SaveRequest;
import com.iso.logus.domain.team.dto.TeamAuthDto.UpdateRequest;
import com.iso.logus.domain.team.exception.TeamAuthNameDuplicationException;
import com.iso.logus.domain.team.exception.TeamAuthNotFoundException;
import com.iso.logus.domain.team.exception.TeamNotFoundException;

import lombok.RequiredArgsConstructor;

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

	public void createTeamAuth(long teamId, SaveRequest saveRequest) {
		if(isExistedTeamAuth(teamId, saveRequest.getName()))
			throw new TeamAuthNameDuplicationException();
		Team team = teamService.findTeamById(teamId);
		teamAuthRepository.save(saveRequest.toEntity(team));
	}

	public void changeTeamAuth(long teamId, String name, UpdateRequest updateRequest) {
		TeamAuth teamAuth = teamAuthRepository.findByTeamIdAndName(teamId, name).orElseThrow(TeamAuthNotFoundException::new);
		teamAuth.update(updateRequest);
	}

	public void deleteTeamAuth(long teamId, String name) {
		teamAuthRepository.deleteByTeamIdAndName(teamId, name);
	}
	
}
