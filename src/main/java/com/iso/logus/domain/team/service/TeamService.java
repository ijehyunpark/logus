package com.iso.logus.domain.team.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iso.logus.domain.team.domain.team.Team;
import com.iso.logus.domain.team.domain.team.TeamRepository;
import com.iso.logus.domain.team.dto.TeamDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamService {

	private final TeamSearchService teamSearchService;
	private final TeamRepository teamRepository;
	private final TeamAuthService teamAuthService;
	
	private static final TeamAuthBaseData baseData = new TeamAuthBaseData();
	
	public Team createTeam(TeamDto.CreateRequest createRequest) {
		Team team = teamRepository.save(createRequest.toEntity());
		teamAuthService.createTeamAuth(baseData.createMasterAuthSaveRequest(team.getId()));
		teamAuthService.createTeamAuth(baseData.createDefaultAuthSaveRequest(team.getId()));
		return team;
	}

	public Team changeTeamDescript(long id, TeamDto.ChangeDescriptRequest changeDescriptRequest) {
		Team team = teamSearchService.findTeamById(id);
		team.changeDescript(changeDescriptRequest);
		return team;
	}
	
	public void deleteTeam(long id) {
		Team team = teamSearchService.findTeamById(id);
		teamRepository.delete(team);
	}
}
