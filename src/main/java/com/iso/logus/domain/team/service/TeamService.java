package com.iso.logus.domain.team.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iso.logus.domain.team.domain.team.Team;
import com.iso.logus.domain.team.domain.team.TeamRepository;
import com.iso.logus.domain.team.domain.teamauth.TeamAuthRepository;
import com.iso.logus.domain.team.dto.TeamDto;
import com.iso.logus.domain.team.exception.TeamNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamService {

	private final TeamRepository teamRepository;
	private final TeamAuthRepository teamAuthRepository;
	
	private final TeamAuthBaseData baseData;
	
	public List<TeamDto.Response> changeResponseDto(List<Team> teamList) {
		List<TeamDto.Response> dtoList = new ArrayList<>();
		for(Team team : teamList)
			dtoList.add(new TeamDto.Response(team));
		return dtoList;
	}
	
	@Transactional(readOnly = true)
	public List<Team> findTeamList() {
		return teamRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	public List<TeamDto.Response> findList() {
		return changeResponseDto(findTeamList());
	}

	@Transactional(readOnly = true)
	public List<Team> findTeamByName(String name) {
		return teamRepository.findAllByName(name);
	}
	
	@Transactional(readOnly = true)
	public List<TeamDto.Response> findByName(String name) {
		return changeResponseDto(findTeamByName(name));
	}
	
	@Transactional(readOnly = true)
	public Team findTeamById(long id) {
		return teamRepository.findById(id).orElseThrow(TeamNotFoundException::new);
	}
	
	public Team createTeam(TeamDto.CreateRequest createRequest) {
		Team team = teamRepository.save(createRequest.toEntity());
		teamRepository.flush();
		teamAuthRepository.save(baseData.createMasterAuth(team));
		teamAuthRepository.save(baseData.createDefaultAuth(team));
		return team;
	}

	public Team changeTeamDescript(long id, TeamDto.ChangeDescriptRequest changeDescriptRequest) {
		Team team = findTeamById(id);
		team.changeDescript(changeDescriptRequest);
		return team;
	}
	
	public void deleteTeam(long id) {
		Team team = findTeamById(id);
		teamRepository.delete(team);
	}
}
