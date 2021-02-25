package com.iso.logus.domain.log.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.iso.logus.domain.log.domain.Log;
import com.iso.logus.domain.team.domain.team.Team;
import com.iso.logus.domain.user.domain.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class LogDto {

	@Getter
	public static class Response {
		private long id;
		private long teamId;
		private String author;
		private String title;
		private String content;
		private Long parentLogId;
		
		public Response(Log log) {
			this.id = log.getId();
			this.teamId = log.getTeam().getId();
			this.author = log.getAuthor().getUid();
			this.title = log.getTitle();
			this.content = log.getContent();
			if(log.getParentLog() != null)
				this.parentLogId = log.getParentLog().getId();
		}
	}
	
	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class LogCreateRequest {
		@NotNull
		private long teamId;
		@NotNull
		private String author;
		@NotEmpty
		private String title;
		@NotEmpty
		private String content;
		private Long parentLogId;
		
		@Builder
		public LogCreateRequest(long teamId, String author, String title, String content, Long parentLogId) {
			this.teamId = teamId;
			this.author = author;
			this.title = title;
			this.content = content;
			this.parentLogId = parentLogId;
		}
		
		public Log toEntity(Team team, User author, Log parentLog) {
			return Log.builder()
						.team(team)
						.author(author)
						.title(title)
						.content(content)
						.parentLog(parentLog)
						.build();
		}
	}
	
	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class ChangeRequest {
		@NotNull
		private long logId;
		@NotEmpty
		private String title;
		@NotEmpty
		private String content;
		
		@Builder
		public ChangeRequest(long logId, String title, String content) {
			this.logId = logId;
			this.title = title;
			this.content = content;
		}
	}
}
