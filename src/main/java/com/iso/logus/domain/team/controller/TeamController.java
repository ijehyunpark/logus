package com.iso.logus.domain.team.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
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

import com.iso.logus.domain.team.domain.teamauth.AuthType;
import com.iso.logus.domain.team.dto.TeamDto;
import com.iso.logus.domain.team.service.TeamSearchService;
import com.iso.logus.domain.team.service.TeamService;
import com.iso.logus.domain.team.service.TeamUserService;
import com.iso.logus.global.exception.AccessDeniedException;
import com.iso.logus.global.jwt.JwtTokenProvider;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/team")
public class TeamController {

	private final TeamService teamService;
	private final TeamUserService teamUserService;
	private final TeamSearchService teamSearchService;
	private final JwtTokenProvider jwtTokenProvider;
	
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
	
	@PatchMapping(value = "/{id}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")
	})
	public void updateTeam(@PathVariable long id, @Valid @RequestBody TeamDto.UpdateRequest UudateRequest, HttpServletRequest request) {
		String token = jwtTokenProvider.resolveToken(request);
		String requestUid = jwtTokenProvider.getUserPk(token);
		if(!teamUserService.isUserHasAuth(id, requestUid, AuthType.teamNameAuth))
			throw new AccessDeniedException();
		teamService.updateTeam(id, UudateRequest);
	}
	
	@DeleteMapping(value = "/{id}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")
	})
	public void deleteTeam(@PathVariable long id, HttpServletRequest request) {
		String token = jwtTokenProvider.resolveToken(request);
		String requestUid = jwtTokenProvider.getUserPk(token);
		if(!teamUserService.isUserHasMasterAuth(id, requestUid))
			throw new AccessDeniedException();
		teamService.deleteTeam(id);
	}
}
