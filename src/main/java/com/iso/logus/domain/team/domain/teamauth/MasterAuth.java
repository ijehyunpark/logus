package com.iso.logus.domain.team.domain.teamauth;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class MasterAuth implements DetailAuth {
	@Column(name = "team_name_auth", columnDefinition = "tinyint(1) not null default false comment '팀 이름 변경 권한'")
	private boolean teamNameAuth;
	
	@Column(name = "auth_manage_auth", columnDefinition = "tinyint(1) not null default false comment '팀 권한 관리 권한'")
	private boolean authManageAuth;
	
	@Column(name = "log_master_auth", columnDefinition = "tinyint(1) not null default false comment '모든 로그 조작 권한'")
	private boolean logMasterAuth;
	
	@Column(name = "calendar_master_auth", columnDefinition = "tinyint(1) not null default false comment '모든 캘린더 조작 권한'")
	private boolean calendarMasterAuth;
	
	@Builder
	private MasterAuth(boolean teamNameAuth, boolean authManageAuth, boolean logMasterAuth, boolean calendarMasterAuth) {
		this.teamNameAuth = teamNameAuth;
		this.authManageAuth = authManageAuth;
		this.logMasterAuth = logMasterAuth;
		this.calendarMasterAuth = calendarMasterAuth;
	}
	
	@Override
	public void makeAllTrue() {
		this.teamNameAuth = true;
		this.authManageAuth = true;
		this.logMasterAuth = true;
		this.calendarMasterAuth = true;
	}

	@Override
	public void makeAllFalse() {
		this.teamNameAuth = false;
		this.authManageAuth = false;
		this.logMasterAuth = false;
		this.calendarMasterAuth = false;
	}

	@Override
	public boolean checkAllTrue() {
		return teamNameAuth && authManageAuth && logMasterAuth && calendarMasterAuth;
	}
}
