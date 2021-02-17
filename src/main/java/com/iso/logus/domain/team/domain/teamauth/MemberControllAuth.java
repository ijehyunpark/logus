package com.iso.logus.domain.team.domain.teamauth;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class MemberControllAuth implements DetailAuth {
	@Column(name = "invite_auth", columnDefinition = "tinyint(1) not null default false comment '팀 초대 권한'")
	private boolean inviteAuth;
	
	@Column(name = "invite_accept_auth", columnDefinition = "tinyint(1) not null default false comment '팀 초대 수락 권한'")
	private boolean inviteAcceptAuth;
	
	@Column(name = "quit_auth", columnDefinition = "tinyint(1) not null default false comment '팀 추방 권한'")
	private boolean quitAuth;
	
	@Builder
	public MemberControllAuth(boolean inviteAuth, boolean inviteAcceptAuth, boolean quitAuth) {
		this.inviteAuth = inviteAuth;
		this.inviteAcceptAuth = inviteAcceptAuth;
		this.quitAuth = quitAuth;
	}

	@Override
	public void makeAllTrue() {
		this.inviteAuth = true;
		this.inviteAcceptAuth = true;
		this.quitAuth = true;
	}

	@Override
	public void makeAllFalse() {
		this.inviteAuth = false;
		this.inviteAcceptAuth = false;
		this.quitAuth = false;
	}

	@Override
	public boolean checkAllTrue() {
		return inviteAuth && inviteAcceptAuth && quitAuth;
	}
}
