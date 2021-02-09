package com.iso.logus.domain.team.dto;

import java.time.LocalDateTime;

import com.iso.logus.domain.team.domain.team.Team;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TeamDto {

	@Getter
	public static class Response {
		private long id;
		private String name;
		private String descript;
		private LocalDateTime createdDate;
		private LocalDateTime lastModifiedDate;
		
		public Response(Team team) {
			this.id = team.getId();
			this.name = team.getName();
			this.descript = team.getDescript();
			this.createdDate = team.getCreatedDate();
			this.lastModifiedDate = team.getLastModifiedDate();
		}
	}
	
	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class CreateRequest {
		private String name;
		private String descript;
		
		@Builder
		public CreateRequest(String name, String descript) {
			this.name = name;
			this.descript = descript;
		}
		
		public Team toEntity() {
			return Team.builder()
						.name(this.name)
						.descript(this.descript)
						.build();
		}
	}
	
	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class ChangeDescriptRequest {
		private String descript;
		
		@Builder
		public ChangeDescriptRequest(String descript) {
			this.descript = descript;
		}
	}
}
