package com.iso.logus.domain.team.service;

import org.springframework.stereotype.Component;

import com.iso.logus.domain.team.domain.team.Team;
import com.iso.logus.domain.team.domain.teamauth.ActiveAuth;
import com.iso.logus.domain.team.domain.teamauth.MasterAuth;
import com.iso.logus.domain.team.domain.teamauth.MemberControllAuth;
import com.iso.logus.domain.team.domain.teamauth.TeamAuth;
import com.iso.logus.domain.team.domain.teamauth.TeamAuthType;
import com.iso.logus.domain.team.dto.TeamAuthDto;

@Component
public class TeamAuthBaseData {
	
	public TeamAuthDto.SaveRequest createMasterAuthSaveRequest(long teamId) {
		return TeamAuthDto.SaveRequest.builder()
					.teamId(teamId)
					.name("Team Master")
					.build();
	}
	
	public TeamAuthDto.UpdateRequest createMasterAuthUpdateRequest(long teamId) {
		MasterAuth masterAuth = new MasterAuth();
		MemberControllAuth memberControllAuth = new MemberControllAuth();
		ActiveAuth activeAuth = new ActiveAuth();
		masterAuth.makeAllTrue();
		memberControllAuth.makeAllTrue();
		activeAuth.makeAllTrue();
		return TeamAuthDto.UpdateRequest.builder()
			.teamId(teamId)
			.originName("Team Master")
			.changeName("Team Master")
			.type(TeamAuthType.MASTER)
			.masterAuth(masterAuth)
			.memberControllAuth(memberControllAuth)
			.activeAuth(activeAuth)
			.build();
	}
	
	public TeamAuth createMasterAuth(Team team) {
		TeamAuth teamAuth = createMasterAuthSaveRequest(team.getId()).toEntity(team);
		teamAuth.update(createMasterAuthUpdateRequest(team.getId()));
		return teamAuth;
	}
	
	public TeamAuthDto.SaveRequest createDefaultAuthSaveRequest(long teamId) {
		return TeamAuthDto.SaveRequest.builder()
					.teamId(teamId)
					.name("Member")
					.build();
	}
	
	public TeamAuthDto.UpdateRequest createDefaultAuthUpdateRequest(long teamId) {
		MasterAuth masterAuth = new MasterAuth();
		MemberControllAuth memberControllAuth = new MemberControllAuth();
		ActiveAuth activeAuth = new ActiveAuth();
		masterAuth.makeAllFalse();
		memberControllAuth.makeAllFalse();
		activeAuth.makeAllFalse();
		return TeamAuthDto.UpdateRequest.builder()
					.teamId(teamId)
					.originName("Member")
					.changeName("Member")
					.type(TeamAuthType.DEFAULT)
					.masterAuth(masterAuth)
					.memberControllAuth(memberControllAuth)
					.activeAuth(activeAuth)
					.build();
	}
	
	public TeamAuth createDefaultAuth(Team team) {
		TeamAuth teamAuth = createDefaultAuthSaveRequest(team.getId()).toEntity(team);
		teamAuth.update(createDefaultAuthUpdateRequest(team.getId()));
		return teamAuth;
	}
}
