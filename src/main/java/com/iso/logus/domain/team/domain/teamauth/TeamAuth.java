package com.iso.logus.domain.team.domain.teamauth;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.iso.logus.domain.team.domain.team.Team;
import com.iso.logus.domain.team.dto.TeamAuthDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
public class TeamAuth {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "team_id", nullable = false)
	private Team team;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "type", columnDefinition = "tinyint(2) not null default 0 comment '권한 분류'")
	private int type;
	
	@Embedded
	private MasterAuth masterAuth;
	
	@Embedded
	private MemberControllAuth memberControllAuth;
	
	@Embedded
	private ActiveAuth activeAuth;
	
	@Builder
	public TeamAuth(Team team, String name, MasterAuth masterAuth, MemberControllAuth memberControllAuth, ActiveAuth activeAuth) {
		this.team = team;
		this.name = name;
		this.masterAuth = masterAuth;
		this.memberControllAuth = memberControllAuth;
		this.activeAuth = activeAuth;
	}
	
	public void update(TeamAuthDto.UpdateRequest updateRequest) {
		this.name = updateRequest.getChangeName();
		this.type = updateRequest.getType().getTeamAuthTypeValue();
		this.masterAuth = updateRequest.getMasterAuth();
		this.memberControllAuth = updateRequest.getMemberControllAuth();
		this.activeAuth = updateRequest.getActiveAuth();
	}
}
