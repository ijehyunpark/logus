package com.iso.logus.domain.team.dto;

import javax.validation.constraints.NotEmpty;

import com.iso.logus.domain.team.domain.team.Team;
import com.iso.logus.domain.team.domain.teamauth.ActiveAuth;
import com.iso.logus.domain.team.domain.teamauth.MasterAuth;
import com.iso.logus.domain.team.domain.teamauth.MemberControllAuth;
import com.iso.logus.domain.team.domain.teamauth.TeamAuth;
import com.iso.logus.domain.team.domain.teamauth.TeamAuthType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TeamAuthDto {

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Response {
		private String name;
		private TeamAuthType type;
		private MasterAuth masterAuth;
		private MemberControllAuth memberControllAuth;
		private ActiveAuth activeAuth;
		
		public Response(TeamAuth teamAuth) {
			this.name = teamAuth.getName();
			this.type = TeamAuthType.values()[teamAuth.getType()];
			this.masterAuth = teamAuth.getMasterAuth();
			this.memberControllAuth = teamAuth.getMemberControllAuth();
			this.activeAuth = teamAuth.getActiveAuth();
		}
	}
	
	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class SaveRequest {
		@NotEmpty
		private String name;
		private MasterAuth masterAuth;
		private MemberControllAuth memberControllAuth;
		private ActiveAuth activeAuth;
		
		@Builder
		public SaveRequest(String name, MasterAuth masterAuth, MemberControllAuth memberControllAuth, ActiveAuth activeAuth) {
			this.name = name;
			this.masterAuth = masterAuth;
			this.memberControllAuth = memberControllAuth;
			this.activeAuth = activeAuth;
		}
		
		public TeamAuth toEntity(Team team) {
			return TeamAuth.builder()
					.team(team)
					.name(name)
					.masterAuth(masterAuth)
					.memberControllAuth(memberControllAuth)
					.activeAuth(activeAuth)
					.build();
		}
	}
	
	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class UpdateRequest {
		@NotEmpty
		private String name;
		private TeamAuthType type;
		private MasterAuth masterAuth;
		private MemberControllAuth memberControllAuth;
		private ActiveAuth activeAuth;
		
		@Builder
		public UpdateRequest(String name, TeamAuthType type, MasterAuth masterAuth, MemberControllAuth memberControllAuth, ActiveAuth activeAuth) {
			this.name = name;
			this.type = type;
			this.masterAuth = masterAuth;
			this.memberControllAuth = memberControllAuth;
			this.activeAuth = activeAuth;
		}
	}
}