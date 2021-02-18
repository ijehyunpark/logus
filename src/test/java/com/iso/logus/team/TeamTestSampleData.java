package com.iso.logus.team;

import org.springframework.stereotype.Component;

import com.iso.logus.domain.team.domain.team.Team;
import com.iso.logus.domain.team.domain.teamauth.ActiveAuth;
import com.iso.logus.domain.team.domain.teamauth.MasterAuth;
import com.iso.logus.domain.team.domain.teamauth.MemberControllAuth;
import com.iso.logus.domain.team.domain.teamauth.TeamAuth;
import com.iso.logus.domain.team.domain.teamauth.TeamAuthType;
import com.iso.logus.domain.team.dto.TeamAuthDto;
import com.iso.logus.domain.team.dto.TeamDto;

@Component
public class TeamTestSampleData {
	
	public TeamDto.CreateRequest makeTeamRequest() {
		return TeamDto.CreateRequest.builder()
				.name("testTeam")
				.descript("sample")
				.build();
	}
	
	public Team makeTeam() {
		return makeTeamRequest().toEntity();
	}
	
	public Team makeTeam(long mockTeamId) {
		Team team = makeTeam();
		team.setIdForTest(mockTeamId);
		return team;
	}
	
	public TeamAuth returnAllTrueAuth(Team team) {
		return returnAllTrueAuth(team, "allTrueAuth");
	}
	public TeamAuth returnAllTrueAuth(Team team, String name) {
		MasterAuth masterTrueAuth = MasterAuth.builder().build();
		MemberControllAuth memberControllTrueAuth = MemberControllAuth.builder().build();
		ActiveAuth activeTrueAuth = ActiveAuth.builder().build();
		masterTrueAuth.makeAllTrue();
		memberControllTrueAuth.makeAllTrue();
		activeTrueAuth.makeAllTrue();
		return TeamAuthDto.SaveRequest.builder()
						.teamId(team.getId())
						.name(name)
						.masterAuth(masterTrueAuth)
						.memberControllAuth(memberControllTrueAuth)
						.activeAuth(activeTrueAuth)
						.build()
						.toEntity(team);
	}
	
	public TeamAuth returnAllFalseAuth(Team team) {
		return returnAllFalseAuth(team, "allFalseAuth");
	}
	
	public TeamAuth returnAllFalseAuth(Team team, String name) {
		MasterAuth masterFalseAuth = MasterAuth.builder().build();
		MemberControllAuth memberControllFalseAuth = MemberControllAuth.builder().build();
		ActiveAuth activeFalseAuth = ActiveAuth.builder().build();
		masterFalseAuth.makeAllFalse();
		memberControllFalseAuth.makeAllFalse();
		activeFalseAuth.makeAllFalse();
		return TeamAuthDto.SaveRequest.builder()
						.teamId(team.getId())
						.name(name)
						.masterAuth(masterFalseAuth)
						.memberControllAuth(memberControllFalseAuth)
						.activeAuth(activeFalseAuth)
						.build()
						.toEntity(team);
	}
	
	public TeamAuth returnSampleAuth(Team team) {
		return returnSampleAuth(team, "sampleAuth");
	}
	
	public TeamAuth returnSampleAuth(Team team, String name) {
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
						.teamId(team.getId())
						.name(name)
						.masterAuth(masterCustomAuth)
						.memberControllAuth(memberControllCustomAuth)
						.activeAuth(activeCustomAuth)
						.build()
						.toEntity(team);
	}
	
	public TeamAuthDto.SaveRequest saveRequestBuilder(long teamId) {
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
				.teamId(teamId)
				.name("new-class")
				.masterAuth(masterAuth)
				.memberControllAuth(memberControllAuth)
				.activeAuth(activeAuth)
				.build();
		return saveRequst;
	}
	
	public TeamAuthDto.UpdateRequest updateRequestBuilder(long teamId, String originName, String changeName, TeamAuthType type) {
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
				.teamId(teamId)
				.originName(originName)
				.changeName(changeName)
				.type(type)
				.masterAuth(masterAuth)
				.memberControllAuth(memberControllAuth)
				.activeAuth(activeAuth)
				.build();
		return updateRequest;
	}
		
	public TeamAuthDto.UpdateRequest updateMasterBuilder(long teamId, String originName, String changeName, TeamAuthType type) {
		MasterAuth masterAuth = MasterAuth.builder()
				.masterAuth(true)
				.teamNameAuth(true)
				.authManageAuth(true)
				.build();
		MemberControllAuth memberControllAuth = MemberControllAuth.builder()
				.inviteAuth(true)
				.inviteAcceptAuth(true)
				.quitAuth(true)
				.build();
		ActiveAuth activeAuth = ActiveAuth.builder()
				.build();
		TeamAuthDto.UpdateRequest updateRequest = TeamAuthDto.UpdateRequest.builder()
				.teamId(teamId)
				.originName(originName)
				.changeName(changeName)
				.type(type)
				.masterAuth(masterAuth)
				.memberControllAuth(memberControllAuth)
				.activeAuth(activeAuth)
				.build();
		return updateRequest;
	}
}
