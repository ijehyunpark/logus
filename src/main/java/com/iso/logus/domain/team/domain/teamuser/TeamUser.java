package com.iso.logus.domain.team.domain.teamuser;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.iso.logus.domain.team.domain.team.Team;
import com.iso.logus.domain.team.domain.teamauth.TeamAuth;
import com.iso.logus.domain.team.dto.TeamUserDto;
import com.iso.logus.domain.user.domain.User;
import com.iso.logus.global.domain.TimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamUser extends TimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "team_id")
	private Team team;
	
	@ManyToOne
	@JoinColumn(name = "user_uid")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "team_auth_id")
	private TeamAuth teamAuth;
	
	@Column(name = "custom_name")
	private String customName;
	
	@Builder
	public TeamUser(Team team, User user, TeamAuth teamAuth, String customName) {
		this.team = team;
		this.user = user;
		this.teamAuth = teamAuth;
		this.customName = customName;
	}
	
	public void updateTeamAuth(TeamAuth teamAuth) {
		this.teamAuth = teamAuth;
	}
	
	public void updateCustomName(TeamUserDto.ChangeCustomNameRequest changeCustomNameRequeste ) {
		this.customName = changeCustomNameRequeste.getCustomName();
	}
}
