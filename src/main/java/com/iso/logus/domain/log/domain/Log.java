package com.iso.logus.domain.log.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.iso.logus.domain.log.dto.LogDto;
import com.iso.logus.domain.team.domain.team.Team;
import com.iso.logus.domain.user.domain.User;
import com.iso.logus.global.domain.TimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Log extends TimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "team_id")
	private Team team;
	
	@ManyToOne
	@JoinColumn(name = "uid", nullable = true)
	private User author;
	
	@Column(name = "title", nullable = false)
	private String title;
	
	@Column(name = "content", nullable = false)
	private String content;
	
	@ManyToOne
	@JoinColumn(name = "parent_log_id", nullable = true)
	private Log parentLog;

	@OneToMany(mappedBy = "parentLog")
	private Set<Log> childLogs = new HashSet<Log>();
	
	@Builder
	public Log(Team team, User author, String title, String content, Log parentLog) {
		this.team = team;
		this.author = author;
		this.title = title;
		this.content = content;
		this.parentLog = parentLog;
	}
	
	public void update(LogDto.ChangeRequest changeRequest) {
		this.title = changeRequest.getTitle();
		this.content = changeRequest.getContent();
	}
	
	public void parentLogDeleteEvent() {
		this.parentLog = null;
	}
}
