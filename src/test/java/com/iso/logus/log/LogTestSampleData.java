package com.iso.logus.log;

import java.util.ArrayList;
import java.util.List;

import org.springframework.test.util.ReflectionTestUtils;

import com.iso.logus.domain.log.domain.Log;
import com.iso.logus.domain.log.dto.LogDto;
import com.iso.logus.domain.team.domain.team.Team;
import com.iso.logus.domain.user.domain.User;

public class LogTestSampleData {
	public LogDto.LogCreateRequest buildCreateRequest(long teamId, String uid) {
		return LogDto.LogCreateRequest.builder()
									.teamId(teamId)
									.author(uid)
									.title("Sample Title")
									.content("Sample Content")
									.build();
	} 
	
	public LogDto.LogCreateRequest buildCreateRequest(long teamId, String uid, long contentPivot) {
		return LogDto.LogCreateRequest.builder()
									.teamId(teamId)
									.author(uid)
									.title("Sample Title" + contentPivot)
									.content("Sample Content" + contentPivot)
									.build();
	}
	
	public Log buildLog(Team team, User user) {
		return buildCreateRequest(team.getId(), user.getUid())
				.toEntity(team, user, null);
	}
	
	public Log buildLog(Team team, User user, long contentPivot) {
		return buildCreateRequest(team.getId(), user.getUid(), contentPivot)
				.toEntity(team, user, null);
	}
	
	public void setMockLogId(Log log, long mockLogId) {
		ReflectionTestUtils.setField(log, "id", mockLogId);
	}
	
	public List<Log> buildLogList(Team team, User user, int idSize) {
		List<Log> logList = new ArrayList<>();
		for(int i = 1; i <= idSize; i++) {
			Log comp = buildLog(team,user,i);
			ReflectionTestUtils.setField(comp, "id", (long) i);
			logList.add(comp);
		}
		return logList;
	}
}
