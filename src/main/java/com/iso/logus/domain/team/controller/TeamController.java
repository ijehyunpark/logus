package com.iso.logus.domain.team.controller;

import java.util.List;

import javax.validation.Valid;

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
import com.iso.logus.domain.team.service.TeamSearchService;
import com.iso.logus.domain.team.service.TeamService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/team")
public class TeamController {

	private final TeamService teamService;
	private final TeamSearchService teamSearchService;
	
	@GetMapping
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")
	})
	public List<TeamDto.Response> findList() {
		return teamSearchService.findList();
	}
	
	@GetMapping(value = "/{name}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")
	})
	public List<TeamDto.Response> findByName(@PathVariable String name) {
		return teamSearchService.findByName(name);
	}
	
	@PostMapping
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")
	})
	@ResponseStatus(code = HttpStatus.CREATED)
	public void createTeam(@Valid @RequestBody TeamDto.CreateRequest createRequest) {
		teamService.createTeam(createRequest);
	}
	
	@PatchMapping(value = "/descript/{id}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")
	})
	public void changeTeamDescript(@PathVariable long id, @Valid @RequestBody TeamDto.ChangeDescriptRequest changeDescriptRequest) {
		teamService.changeTeamDescript(id, changeDescriptRequest);
	}
	
	@DeleteMapping(value = "/{id}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")
	})
	public void deleteTeam(@PathVariable long id) {
		teamService.deleteTeam(id);
	}
}
