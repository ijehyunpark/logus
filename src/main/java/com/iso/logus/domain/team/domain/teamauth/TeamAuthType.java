package com.iso.logus.domain.team.domain.teamauth;

public enum TeamAuthType {
	NONE(0),
	MASTER(1),
	DEFAULT(2);
	
	private final int value;
	private TeamAuthType(int value) {
		this.value = value;
	}
	
	public int getTeamAuthTypeValue() {
		return this.value;
	}
}
