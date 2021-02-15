package com.iso.logus.team.teamauth;

import org.springframework.stereotype.Component;

import com.iso.logus.domain.team.domain.team.Team;
import com.iso.logus.domain.team.domain.teamauth.ActiveAuth;
import com.iso.logus.domain.team.domain.teamauth.MasterAuth;
import com.iso.logus.domain.team.domain.teamauth.MemberControllAuth;
import com.iso.logus.domain.team.domain.teamauth.TeamAuth;
import com.iso.logus.domain.team.dto.TeamAuthDto;
import com.iso.logus.domain.team.dto.TeamDto;

@Component
public class TeamAuthSampleData {
	
	protected Team makeTeam() {
		Team team = TeamDto.CreateRequest.builder()
				.name("testTeam")
				.descript("sample")
				.build().toEntity();
		return team;
	}
	protected Team makeTeam(long mockTeamId) {
		Team team = makeTeam();
		team.setIdForTest(mockTeamId);
		return team;
	}
	
	protected TeamAuth returnAllTrueAuth(Team team) {
		MasterAuth masterTrueAuth = MasterAuth.builder().build();
		MemberControllAuth memberControllTrueAuth = MemberControllAuth.builder().build();
		ActiveAuth activeTrueAuth = ActiveAuth.builder().build();
		masterTrueAuth.setAllTrue();
		memberControllTrueAuth.setAllTrue();
		activeTrueAuth.setAllTrue();
		return TeamAuthDto.SaveRequest.builder()
						.name("first-class")
						.masterAuth(masterTrueAuth)
						.memberControllAuth(memberControllTrueAuth)
						.activeAuth(activeTrueAuth)
						.build()
						.toEntity(team);
	}
	
	protected TeamAuth returnAllFalseAuth(Team team) {
		MasterAuth masterFalseAuth = MasterAuth.builder().build();
		MemberControllAuth memberControllFalseAuth = MemberControllAuth.builder().build();
		ActiveAuth activeFalseAuth = ActiveAuth.builder().build();
		masterFalseAuth.setAllFalse();
		memberControllFalseAuth.setAllFalse();
		activeFalseAuth.setAllFalse();
		return TeamAuthDto.SaveRequest.builder()
						.name("last-class")
						.masterAuth(masterFalseAuth)
						.memberControllAuth(memberControllFalseAuth)
						.activeAuth(activeFalseAuth)
						.build()
						.toEntity(team);
	}
	
	protected TeamAuth returnSampleAuth(Team team) {
		MasterAuth masterCustomAuth = MasterAuth.builder()
				.masterAuth(false)
				.teamNameAuth(true)
				.authManageAuth(false)
				.build();
		MemberControllAuth memberControllCustomAuth = MemberControllAuth.builder()
				.inviteAuth(true)
				.inviteAcceptAuth(true)
				.quitAuth(false)
				.build();
		ActiveAuth activeCustomAuth = ActiveAuth.builder()
				.build();
		return TeamAuthDto.SaveRequest.builder()
						.name("special-class")
						.masterAuth(masterCustomAuth)
						.memberControllAuth(memberControllCustomAuth)
						.activeAuth(activeCustomAuth)
						.build()
						.toEntity(team);
	}
	
	protected TeamAuthDto.SaveRequest saveRequestBuilder() {
		MasterAuth masterAuth = MasterAuth.builder()
				.masterAuth(false)
				.teamNameAuth(false)
				.authManageAuth(false)
				.build();
		MemberControllAuth memberControllAuth = MemberControllAuth.builder()
				.inviteAuth(true)
				.inviteAcceptAuth(false)
				.quitAuth(false)
				.build();
		ActiveAuth activeAuth = ActiveAuth.builder()
				.build();
		TeamAuthDto.SaveRequest saveRequst = TeamAuthDto.SaveRequest.builder()
				.name("new-class")
				.masterAuth(masterAuth)
				.memberControllAuth(memberControllAuth)
				.activeAuth(activeAuth)
				.build();
		return saveRequst;
	}
	
	protected TeamAuthDto.UpdateRequest updateRequestBuilder() {
		MasterAuth masterAuth = MasterAuth.builder()
				.masterAuth(false)
				.teamNameAuth(false)
				.authManageAuth(false)
				.build();
		MemberControllAuth memberControllAuth = MemberControllAuth.builder()
				.inviteAuth(true)
				.inviteAcceptAuth(false)
				.quitAuth(false)
				.build();
		ActiveAuth activeAuth = ActiveAuth.builder()
				.build();
		TeamAuthDto.UpdateRequest updateRequest = TeamAuthDto.UpdateRequest.builder()
				.name("updated")
				.masterAuth(masterAuth)
				.memberControllAuth(memberControllAuth)
				.activeAuth(activeAuth)
				.build();
		return updateRequest;
	}
}
