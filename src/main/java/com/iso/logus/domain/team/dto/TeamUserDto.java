package com.iso.logus.domain.team.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iso.logus.domain.team.domain.team.Team;
import com.iso.logus.domain.team.domain.teamauth.TeamAuth;
import com.iso.logus.domain.team.domain.teamauth.TeamAuthType;
import com.iso.logus.domain.team.domain.teamuser.TeamUser;
import com.iso.logus.domain.user.domain.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TeamUserDto {

	@Getter
	public static class MemberResponse {
		private String uid;
		private String teamAuthName;
		private boolean teamMaster;
		private String customName = "hello";
		
		public MemberResponse(TeamUser teamUser) {
			this.uid = teamUser.getUser().getUid();
			this.teamAuthName = teamUser.getTeamAuth().getName();
			this.teamMaster = TeamAuthType.MASTER == TeamAuthType.values()[teamUser.getTeamAuth().getType()];
			this.customName = teamUser.getCustomName();
		}
	}
	
	@Getter
	private static class TeamResponseData {
		private Long id;
		private String name;
		private String descript;
		private LocalDateTime createdDate;
		private LocalDateTime lastModifiedDate;
		
		public TeamResponseData(Team team) {
			this.id = team.getId();
			this.name = team.getName();
			this.descript = team.getDescript();
			this.createdDate = team.getCreatedDate();
			this.lastModifiedDate = team.getLastModifiedDate();
		}
	}
	
	@Getter
	public static class TeamResponse {
		private TeamResponseData team;
		
		public TeamResponse(TeamUser teamUser) {
			this.team = new TeamResponseData(teamUser.getTeam());
		}
	}
	
	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class JoinRequest {
		@NotNull
		private long teamId;
		@NotEmpty
		private String uid;
		private String customName;
		
		@Builder
		public JoinRequest(long teamId, String uid, String customName) {
			this.teamId = teamId;
			this.uid = uid;
			this.customName = customName;
		}
		
		public TeamUser toEntity(Team team, User user, TeamAuth teamAuth) {
			return TeamUser.builder()
						.team(team)
						.user(user)
						.teamAuth(teamAuth)
						.customName(customName)
						.build();
		}
	}
	
	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class QuitRequest {
		@NotNull
		private long teamId;
		@NotEmpty
		private String uid;
		
		@Builder
		public QuitRequest(long teamId, String uid) {
			this.teamId = teamId;
			this.uid = uid;
		}
	}
	
	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class ChangeAuthRequest {
		@NotNull
		private long teamId;
		@NotEmpty
		private String uid;
		@NotEmpty
		private String teamAuthName;
		
		@Builder
		public ChangeAuthRequest(long teamId, String uid, String teamAuthName) {
			this.teamId = teamId;
			this.uid = uid;
			this.teamAuthName = teamAuthName;
		}
	}
	
	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class ChangeCustomNameRequest {
		@NotNull
		private long teamId;
		@NotEmpty
		private String uid;
		private String customName;
		
		@Builder
		public ChangeCustomNameRequest(long teamId, String uid, String customName) {
			this.teamId = teamId;
			this.uid = uid;
			this.customName = customName;
		}
	}
}
