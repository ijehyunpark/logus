package com.iso.logus.domain.team.domain.teamauth;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class ActiveAuth implements DetailAuth {
	@Column(name = "log_write_auth", columnDefinition = "tinyint(1) not null default false comment '글 쓰기 권한'")
	private boolean logWriteAuth;
	
	@Column(name = "reply_write_auth", columnDefinition = "tinyint(1) not null default false comment '댓글 쓰기 권한'")
	private boolean replyWriteAuth;
	
	@Column(name = "to_do_auth", columnDefinition = "tinyint(1) not null default false comment 'toDoList 조작 권한'")
	private boolean toDoAuth;
	
	@Column(name = "calendar_auth", columnDefinition = "tinyint(1) not null default false comment '캘린더 조작 권한'")
	private boolean calendarAuth;
	
	@Builder
	public ActiveAuth(boolean logWriteAuth, boolean replyWriteAuth, boolean toDoAuth, boolean calendarAuth) {
		this.logWriteAuth = logWriteAuth;
		this.replyWriteAuth = replyWriteAuth;
		this.toDoAuth = toDoAuth;
		this.calendarAuth = calendarAuth;
	}
	
	@Override
	public void makeAllTrue() {
		this.logWriteAuth = true;
		this.replyWriteAuth = true;
		this.toDoAuth = true;
		this.calendarAuth = true;
	}

	@Override
	public void makeAllFalse() {
		this.logWriteAuth = false;
		this.replyWriteAuth = false;
		this.toDoAuth = false;
		this.calendarAuth = false;
	}

	@Override
	public boolean checkAllTrue() {
		return logWriteAuth && replyWriteAuth && toDoAuth && calendarAuth;
	}
}
