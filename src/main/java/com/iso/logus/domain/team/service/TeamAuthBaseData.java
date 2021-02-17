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
	public TeamAuth createMasterAuth(Team team) {
		MasterAuth masterAuth = new MasterAuth();
		masterAuth.makeAllTrue();
		MemberControllAuth memberControllAuth = new MemberControllAuth();
		memberControllAuth.makeAllTrue();
		ActiveAuth activeAuth = new ActiveAuth();
		activeAuth.makeAllTrue();
		TeamAuth teamAuth = TeamAuthDto.SaveRequest.builder()
					.name("Team Master")
					.build()
					.toEntity(team);
		
		teamAuth.update(TeamAuthDto.UpdateRequest.builder()
					.name("Team Master")
					.type(TeamAuthType.MASTER)
					.masterAuth(masterAuth)
					.memberControllAuth(memberControllAuth)
					.activeAuth(activeAuth)
					.build());
		return teamAuth;
	}
	
	public TeamAuth createDefaultAuth(Team team) {
		MasterAuth masterAuth = new MasterAuth();
		masterAuth.makeAllFalse();
		MemberControllAuth memberControllAuth = new MemberControllAuth();
		memberControllAuth.makeAllFalse();
		ActiveAuth activeAuth = new ActiveAuth();
		activeAuth.makeAllFalse();
		TeamAuth teamAuth = TeamAuthDto.SaveRequest.builder()
					.name("Member")
					.build()
					.toEntity(team);
		
		teamAuth.update(TeamAuthDto.UpdateRequest.builder()
					.name("Member")
					.type(TeamAuthType.DEFAULT)
					.masterAuth(masterAuth)
					.memberControllAuth(memberControllAuth)
					.activeAuth(activeAuth)
					.build());
		return teamAuth;
	}
}
