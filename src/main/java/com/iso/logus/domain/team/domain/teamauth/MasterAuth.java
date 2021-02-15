package com.iso.logus.domain.team.domain.teamauth;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MasterAuth implements DetailAuth {
	@Column(name = "master_auth", columnDefinition = "tinyint(1) not null default false comment '최종 관리자 권한'")
	private boolean masterAuth;
	
	@Column(name = "team_name_auth", columnDefinition = "tinyint(1) not null default false comment '팀 이름 변경 권한'")
	private boolean teamNameAuth;
	
	@Column(name = "auth_manage_auth", columnDefinition = "tinyint(1) not null default false comment '팀 권한 관리 권한'")
	private boolean authManageAuth;
	
//	@Column(name = "log_master_auth", columnDefinition = "tinyint(1) not null default false comment '모든 로그 조작 권한'")
//	private boolean logMasterAuth;
	
//	@Column(name = "calendar_master_auth", columnDefinition = "tinyint(1) not null default false comment '모든 캘린더 조작 권한'")
//	private boolean calendarMasterAuth;
	
	@Builder
	public MasterAuth(boolean masterAuth, boolean teamNameAuth, boolean authManageAuth) {
		this.masterAuth = masterAuth;
		this.teamNameAuth = teamNameAuth;
		this.authManageAuth = authManageAuth;
	}
	
	@Override
	public void setAllTrue() {
		this.masterAuth = true;
		this.teamNameAuth = true;
		this.authManageAuth = true;
	}

	@Override
	public void setAllFalse() {
		this.masterAuth = false;
		this.teamNameAuth = false;
		this.authManageAuth = false;
	}
}
