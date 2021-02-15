package com.iso.logus.domain.team.domain.teamauth;

import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActiveAuth implements DetailAuth {
	//private boolean logWriteAuth;
	
	//private boolean replyWriteAuth;
	
	//private boolean todoWriteAuth;
	
	//private boolean todoControllAuth;
	
	//private boolean calendarAuth;
	
	@Builder
	public ActiveAuth(boolean noContent) {
		
	}
	
	@Override
	public void setAllTrue() {
		
	}

	@Override
	public void setAllFalse() {
		
	}
}
