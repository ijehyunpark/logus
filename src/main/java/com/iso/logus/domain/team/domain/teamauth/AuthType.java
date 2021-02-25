package com.iso.logus.domain.team.domain.teamauth;

public enum AuthType {
	/* master auth */
	teamnameauth("teamNameAuth", "MasterAuth"),
	authmanageauth("authManageAuth", "MasterAuth"),
	logmasterauth("logMasterAuth", "MasterAuth"),
	calendarmasterauth("calendarMasterAuth", "MasterAuth"),
	
	/* member controll auth */
	inviteauth("inviteAuth", "MemberControllAuth"),
	inviteacceptAuth("inviteAcceptAuth", "MemberControllAuth"),
	quitauth("quitAuth", "MemberControllAuth"),
	
	/* active auth */
	logwriteauth("logWirteAuth", "ActiveAuth"),
	replywriteauth("replyWriteAuth", "ActiveAuth"),
	todoauth("toDoAuth", "ActiveAuth"),
	calendarauth("calendarAuth", "ActiveAuth");
	
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
