package com.iso.logus.domain.team.controller;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.iso.logus.domain.team.domain.teamauth.AuthType;
import com.iso.logus.domain.team.dto.TeamAuthDto;
import com.iso.logus.domain.team.service.TeamAuthService;
import com.iso.logus.domain.team.service.TeamUserService;
import com.iso.logus.global.exception.AccessDeniedException;
import com.iso.logus.global.jwt.JwtTokenProvider;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/team/auth")
public class TeamAuthController {
	
	private final TeamAuthService teamAuthService;
	private final TeamUserService teamUserService;
	private final JwtTokenProvider jwtTokenProvider;

	@GetMapping(value = "/{teamId}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")
	})
	public List<TeamAuthDto.Response> findTeamAuthList(@PathVariable long teamId) {
		return teamAuthService.findList(teamId);
	}
	
	@PostMapping
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")
	})
	@ResponseStatus(code = HttpStatus.CREATED)
	public void createTeamAuth(@Valid @RequestBody TeamAuthDto.SaveRequest saveRequest, HttpServletRequest request) {
		String token = jwtTokenProvider.resolveToken(request);
		String requestUid = jwtTokenProvider.getUserPk(token);
		if(!teamUserService.isUserHasAuth(saveRequest.getTeamId(), requestUid, AuthType.authManageAuth))
			throw new AccessDeniedException();
		teamAuthService.createTeamAuth(saveRequest);
	}
	
	@PutMapping
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")
	})
	public void changeTeamAuth(@Valid @RequestBody TeamAuthDto.UpdateRequest updateRequest, HttpServletRequest request) {
		String token = jwtTokenProvider.resolveToken(request);
		String requestUid = jwtTokenProvider.getUserPk(token);
		if(!teamUserService.isUserHasAuth(updateRequest.getTeamId(), requestUid, AuthType.authManageAuth))
			throw new AccessDeniedException();
		teamAuthService.changeTeamAuth(updateRequest);
	}
	
	@DeleteMapping(value = "/{teamId}/{authName}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")
	})
	public void deleteTeamAuth(@PathVariable long teamId, @PathVariable String authName, HttpServletRequest request) {
		String token = jwtTokenProvider.resolveToken(request);
		String requestUid = jwtTokenProvider.getUserPk(token);
		if(!teamUserService.isUserHasAuth(teamId, requestUid, AuthType.authManageAuth))
			throw new AccessDeniedException();
		teamAuthService.deleteTeamAuth(teamId, authName);
	}
}
