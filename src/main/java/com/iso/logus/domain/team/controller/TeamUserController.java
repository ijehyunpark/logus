package com.iso.logus.domain.team.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iso.logus.domain.team.domain.teamauth.AuthType;
import com.iso.logus.domain.team.dto.TeamUserDto;
import com.iso.logus.domain.team.service.TeamUserService;
import com.iso.logus.global.exception.AccessDeniedException;
import com.iso.logus.global.jwt.JwtTokenProvider;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/team/member")
public class TeamUserController {
	
	private final TeamUserService teamUserService;
	
	private final JwtTokenProvider jwtTokenProvider;
	
	/* Search */
	@ApiImplicitParams({@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")})
	@GetMapping(value = "/auth-test/{teamId}/{uid}/{authType}")
	public Map<String, Boolean> isUserHasAuth(@PathVariable long teamId, @PathVariable String uid, @PathVariable AuthType authType) {
		return Collections.singletonMap("value", teamUserService.isUserHasAuth(teamId, uid, authType));
	}
	
	@ApiImplicitParams({@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")})
	@GetMapping(value = "/find/member/{teamId}")
	public List<TeamUserDto.MemberResponse> findAllMemberByTeam(@PathVariable long teamId) {
		return teamUserService.findAllMemberByTeam(teamId);
	}
	
	@ApiImplicitParams({@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")})
	@GetMapping(value = "/find/team/{uid}")
	public List<TeamUserDto.TeamResponse> findAllTeamByUser(@PathVariable String uid) {
		return teamUserService.findAllTeamByUser(uid);
	}
	
	/* Manage team member */
	@PostMapping(value = "/join")
	public void joinNewMember(@Valid @RequestBody TeamUserDto.JoinRequest joinRequest) {
		teamUserService.joinNewMember(joinRequest);
	}
	
	@PostMapping(value = "/quit")
	public void quitMember(@Valid @RequestBody TeamUserDto.QuitRequest quitRequest, HttpServletRequest request) {
		String token = jwtTokenProvider.resolveToken(request);
		String requestUid = jwtTokenProvider.getUserPk(token);
		if(!teamUserService.isUserHasMasterAuth(quitRequest.getTeamId(), requestUid) && !jwtTokenProvider.validateUser(quitRequest.getUid(), request))
			throw new AccessDeniedException();
		teamUserService.quitMember(quitRequest);
	}
	
	/* Manage member detail */
	@PatchMapping(value = "/auth")
	public void changeAuth(@Valid @RequestBody TeamUserDto.ChangeAuthRequest changeAuthRequest, HttpServletRequest request) {
		String token = jwtTokenProvider.resolveToken(request);
		String requestUid = jwtTokenProvider.getUserPk(token);
		if(!teamUserService.isUserHasAuth(changeAuthRequest.getTeamId(), requestUid, AuthType.authManageAuth))
			throw new AccessDeniedException();
		teamUserService.changeAuth(changeAuthRequest);
	}
	
	@PatchMapping(value = "/name")
	public void changeCustomName(@Valid @RequestBody TeamUserDto.ChangeCustomNameRequest customNameRequest) {
		teamUserService.changeCustomName(customNameRequest);
	}
}
