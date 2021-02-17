package com.iso.logus.domain.team.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iso.logus.domain.team.dto.TeamUserDto;
import com.iso.logus.domain.team.service.TeamUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/team/member")
public class TeamUserController {
	
	private final TeamUserService teamUserService;
	
	/* Search */
	@GetMapping(value = "/find/member/{team_id}")
	public List<TeamUserDto.MemberResponse> findAllMemberByTeam(@PathVariable long team_id) {
		return teamUserService.findAllMemberByTeam(team_id);
	}
	
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
	public void quitMember(@Valid @RequestBody TeamUserDto.QuitRequest quitRequest) {
		teamUserService.quitMember(quitRequest);
	}
	
	/* Manage member detail */
	@PatchMapping(value = "/auth")
	public void changeAuth(@Valid @RequestBody TeamUserDto.ChangeAuthRequest changeAuthRequest) {
		teamUserService.changeAuth(changeAuthRequest);
	}
	
	@PatchMapping(value = "/name")
	public void changeCustomName(@Valid @RequestBody TeamUserDto.ChangeCustomNameRequest customNameRequest) {
		teamUserService.changeCustomName(customNameRequest);
	}
}
