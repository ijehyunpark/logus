package com.iso.logus.domain.log.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iso.logus.domain.log.domain.KeywordType;
import com.iso.logus.domain.log.domain.Log;
import com.iso.logus.domain.log.domain.LogRepository;
import com.iso.logus.domain.log.exception.LogNotFoundException;
import com.iso.logus.domain.team.exception.TeamNotFoundException;
import com.iso.logus.global.exception.ServerErrorException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LogSearchService {

	private final LogRepository logRepository;

	public Log findLogById(long id) {
		return logRepository.findById(id).orElseThrow(LogNotFoundException::new);
	}

	public Page<Log> findPageLogByTeamId(long teamId, Pageable pageable) {
		Page<Log> logList = logRepository.findAllByTeamId(teamId, pageable);
		if(logList.isEmpty())
			throw new TeamNotFoundException();
		return logList;
	}

	public Page<Log> findPageLogByTeamIdAndKeyword(long teamId, KeywordType type, String keyword, Pageable pageable) {
		if(type == KeywordType.titleonly)
			return logRepository.findAllByTeamIdAndTitleContaining(teamId, keyword, pageable);
		if(type == KeywordType.contentonly)
			return logRepository.findAllByTeamIdAndContentContaining(teamId, keyword, pageable);
		if(type == KeywordType.author)
			return logRepository.findAllByTeamIdAndAuthorContaining(teamId, keyword, pageable);
		if(type == KeywordType.titleandcontent)
			return logRepository.findAllByTeamIdAndKeywordContaining(teamId, keyword, pageable);
		throw new ServerErrorException();
	}
}
