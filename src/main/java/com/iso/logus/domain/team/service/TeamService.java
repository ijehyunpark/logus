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
	

	
	public Team createTeam(TeamDto.CreateRequest createRequest) {
		Team team = teamRepository.save(createRequest.toEntity());
		teamAuthService.setUpTeamAuth(team);
		return team;
	}

	public Team updateTeam(long id, TeamDto.UpdateRequest updateRequest) {
		Team team = teamSearchService.findTeamById(id);
		team.update(updateRequest);
		return team;
	}
	
	public void deleteTeam(long id) {
		Team team = teamSearchService.findTeamById(id);
		teamRepository.delete(team);
	}
}
