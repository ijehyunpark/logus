package com.iso.logus.domain.log.service;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.iso.logus.domain.log.domain.Log;
import com.iso.logus.domain.log.domain.LogRepository;
import com.iso.logus.domain.log.dto.LogDto;
import com.iso.logus.domain.log.dto.LogDto.LogCreateRequest;
import com.iso.logus.domain.team.domain.team.Team;
import com.iso.logus.domain.team.service.TeamSearchService;
import com.iso.logus.domain.user.domain.User;
import com.iso.logus.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogService {

	private final LogRepository logRepository;
	private final TeamSearchService teamSearchService;
	private final UserService userService;
	private final LogSearchService logSearchService;

	public Log writeLog(LogCreateRequest createRequest) {
		Team team = teamSearchService.findTeamById(createRequest.getTeamId());
		User user = userService.findUserByUid(createRequest.getAuthor());
		Log parentLog = null;
		if(createRequest.getParentLogId() != null)
			parentLog = logSearchService.findLogById(createRequest.getParentLogId());
		return logRepository.save(createRequest.toEntity(team, user, parentLog));
	}

	public void changeLog(@Valid LogDto.ChangeRequest changeRequest) {
		Log log = logSearchService.findLogById(changeRequest.getLogId());
		log.update(changeRequest);
	}

	public void deleteLog(long logId) {
		Log log = logSearchService.findLogById(logId);
		for(Log childLog : log.getChildLogs())
			childLog.parentLogDeleteEvent();
		logRepository.delete(log);
	}
}
