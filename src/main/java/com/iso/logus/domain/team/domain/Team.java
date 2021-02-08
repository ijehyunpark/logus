package com.iso.logus.domain.team.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
	
	@Builder
	public Team(String name, String descript) {
		this.name = name;
		this.descript = descript;
	}
	
	public void changeDescript(TeamDto.ChangeDescriptRequest changeDescriptRequest) {
		this.descript = changeDescriptRequest.getDescript();
	}
}
