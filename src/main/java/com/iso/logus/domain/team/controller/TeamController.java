package com.iso.logus.domain.team.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.iso.logus.domain.team.dto.TeamDto;
import com.iso.logus.domain.team.service.TeamService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/team")
public class TeamController {

	private final TeamService teamService;
	
	@GetMapping
	public List<TeamDto.Response> findList() {
		return teamService.findList();
	}
	
	@GetMapping(value = "/{name}")
	public List<TeamDto.Response> findByName(@PathVariable String name) {
		return teamService.findByName(name);
	}
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public void createTeam(@RequestBody TeamDto.CreateRequest createRequest) {
		teamService.createTeam(createRequest);
	}
	
	@PatchMapping(value = "/descript/{id}")
	public void changeTeamDescript(@PathVariable long id, @RequestBody TeamDto.ChangeDescriptRequest changeDescriptRequest) {
		teamService.changeTeamDescript(id, changeDescriptRequest);
	}
	
	@DeleteMapping(value = "/{id}")
	public void deleteTeam(@PathVariable long id) {
		teamService.deleteTeam(id);
	}
}
