package com.iso.logus.domain.team.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iso.logus.domain.team.domain.team.Team;
import com.iso.logus.domain.team.domain.team.TeamRepository;
import com.iso.logus.domain.team.dto.TeamDto;
import com.iso.logus.domain.team.exception.TeamNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamSearchService {

	private final TeamRepository teamRepository;
	
	public List<TeamDto.Response> changeResponseDto(List<Team> teamList) {
		List<TeamDto.Response> dtoList = new ArrayList<>();
		for(Team team : teamList)
			dtoList.add(new TeamDto.Response(team));
		return dtoList;
	}
	
	public List<Team> findTeamList() {
		return teamRepository.findAll();
	}
	
	public List<TeamDto.Response> findList() {
		return changeResponseDto(findTeamList());
	}

	public List<Team> findTeamByName(String name) {
		return teamRepository.findAllByName(name);
	}
	
	public List<TeamDto.Response> findByName(String name) {
		return changeResponseDto(findTeamByName(name));
	}
	
	public Team findTeamById(long id) {
		return teamRepository.findById(id).orElseThrow(TeamNotFoundException::new);
	}
	
	public boolean isExistsTeamById(long id) {
		return teamRepository.existsById(id);
	}
}
