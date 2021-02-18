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
	
	MasterAuth masterAuth = new MasterAuth();
	MemberControllAuth memberControllAuth = new MemberControllAuth();
	ActiveAuth activeAuth = new ActiveAuth();
	
	public TeamAuthDto.SaveRequest createMasterAuthSaveRequest(long teamId) {
		masterAuth.makeAllTrue();
		memberControllAuth.makeAllTrue();
		activeAuth.makeAllTrue();
		return TeamAuthDto.SaveRequest.builder()
					.teamId(teamId)
					.name("Team Master")
					.build();
	}
	
	public TeamAuth createMasterAuth(Team team) {
		TeamAuth teamAuth = createMasterAuthSaveRequest(team.getId()).toEntity(team);
		
		teamAuth.update(TeamAuthDto.UpdateRequest.builder()
					.teamId(team.getId())
					.originName("Team Master")
					.changeName("Team Master")
					.type(TeamAuthType.MASTER)
					.masterAuth(masterAuth)
					.memberControllAuth(memberControllAuth)
					.activeAuth(activeAuth)
					.build());
		return teamAuth;
	}
	
	public TeamAuthDto.SaveRequest createDefaultAuthSaveRequest(long teamId) {
		masterAuth.makeAllFalse();
		memberControllAuth.makeAllFalse();
		activeAuth.makeAllFalse();
		return TeamAuthDto.SaveRequest.builder()
					.teamId(teamId)
					.name("Member")
					.build();
	}
	
	public TeamAuth createDefaultAuth(Team team) {
		TeamAuth teamAuth = createDefaultAuthSaveRequest(team.getId()).toEntity(team);
		
		teamAuth.update(TeamAuthDto.UpdateRequest.builder()
					.teamId(team.getId())
					.originName("Member")
					.changeName("Member")
					.type(TeamAuthType.DEFAULT)
					.masterAuth(masterAuth)
					.memberControllAuth(memberControllAuth)
					.activeAuth(activeAuth)
					.build());
		return teamAuth;
	}
}
