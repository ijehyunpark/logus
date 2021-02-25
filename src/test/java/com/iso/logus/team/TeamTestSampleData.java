package com.iso.logus.team;

import org.springframework.test.util.ReflectionTestUtils;

import com.iso.logus.domain.team.domain.team.Team;
import com.iso.logus.domain.team.domain.teamauth.ActiveAuth;
import com.iso.logus.domain.team.domain.teamauth.MasterAuth;
import com.iso.logus.domain.team.domain.teamauth.MemberControllAuth;
import com.iso.logus.domain.team.domain.teamauth.TeamAuth;
import com.iso.logus.domain.team.domain.teamauth.TeamAuthType;
import com.iso.logus.domain.team.dto.TeamAuthDto;
import com.iso.logus.domain.team.dto.TeamDto;

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
		ReflectionTestUtils.setField(team, "id", mockTeamId);
		return team;
	}
	
	public TeamAuth returnAllTrueAuth(Team team) {
		return returnAllTrueAuth(team, "allTrueAuth");
	}
	public TeamAuth returnAllTrueAuth(Team team, String name) {
		MasterAuth masterTrueAuth = new MasterAuth();
		MemberControllAuth memberControllTrueAuth = new MemberControllAuth();
		ActiveAuth activeTrueAuth = new ActiveAuth();
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
		MasterAuth masterFalseAuth = new MasterAuth();
		MemberControllAuth memberControllFalseAuth = new MemberControllAuth();
		ActiveAuth activeFalseAuth = new ActiveAuth();
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
		MasterAuth masterAuth = new MasterAuth();
		masterAuth.makeAllTrue();
		MemberControllAuth memberControllAuth = new MemberControllAuth();
		memberControllAuth.makeAllTrue();
		ActiveAuth activeAuth = new ActiveAuth();
		activeAuth.makeAllTrue();
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
