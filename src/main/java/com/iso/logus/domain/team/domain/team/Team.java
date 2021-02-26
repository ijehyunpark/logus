package com.iso.logus.domain.team.domain.team;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.iso.logus.domain.log.domain.Log;
import com.iso.logus.domain.team.domain.teamauth.TeamAuth;
import com.iso.logus.domain.team.domain.teamuser.TeamUser;
import com.iso.logus.domain.team.dto.TeamDto;
import com.iso.logus.global.domain.TimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team extends TimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "descript")
	private String descript;
	
	@OneToMany(mappedBy = "team", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<TeamAuth> teamAuths = new HashSet<>();
	
	@OneToMany(mappedBy = "team", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<TeamUser> teamUsers = new HashSet<>();
	
	@OneToMany(mappedBy = "team", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<Log> logList = new HashSet<>();
	
	@Builder
	private Team(String name, String descript) {
		this.name = name;
		this.descript = descript;
	}
	
	public void update(TeamDto.UpdateRequest updateRequest) {
		Optional<String> updateName = Optional.ofNullable(updateRequest.getName());
		this.name = updateName.orElse(this.name);
		Optional<String> updateDescript = Optional.ofNullable(updateRequest.getDescript());
		this.descript = updateDescript.orElse(this.descript);
	}
	
	public void setIdForTest(long mockId) {
		this.id = mockId;
	}
}
