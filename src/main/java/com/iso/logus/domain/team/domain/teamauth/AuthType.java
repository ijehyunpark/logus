package com.iso.logus.domain.team.domain.teamauth;

public enum AuthType {
	/* master auth */
	teamNameAuth("teamNameAuth", "MasterAuth"),
	authManageAuth("authManageAuth", "MasterAuth"),
	logMasterAuth("logMasterAuth", "MasterAuth"),
	calendarMasterAuth("calendarMasterAuth", "MasterAuth"),
	
	/* member controll auth */
	inviteAuth("inviteAuth", "MemberControllAuth"),
	inviteAcceptAuth("inviteAcceptAuth", "MemberControllAuth"),
	quitAuth("quitAuth", "MemberControllAuth"),
	
	/* active auth */
	logWriteAuth("logWirteAuth", "ActiveAuth"),
	replyWriteAuth("replyWriteAuth", "ActiveAuth"),
	toDoAuth("toDoAuth", "ActiveAuth"),
	calendarAuth("calendarAuth", "ActiveAuth");
	
	private final String authFieldName;
	private final String authClassName;
	
	private AuthType (String authFieldName, String authClassName) {
		this.authFieldName = authFieldName;
		this.authClassName = authClassName;
	}
	
	public String getAuthType() {
		return this.authFieldName;
	}
	
	public String getAuthClassName() {
		return this.authClassName;
	}
}
