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

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamAuth {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "team_id", nullable = false)
	private Team team;
	
	@Column(name = "name", nullable = false)
	private String name;
	
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
		this.name = updateRequest.getName();
		this.masterAuth = updateRequest.getMasterAuth();
		this.memberControllAuth = updateRequest.getMemberControllAuth();
		this.activeAuth = updateRequest.getActiveAuth();
	}
}
