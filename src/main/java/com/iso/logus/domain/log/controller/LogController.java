package com.iso.logus.domain.log.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.iso.logus.domain.log.domain.KeywordType;
import com.iso.logus.domain.log.dto.LogDto;
import com.iso.logus.domain.log.service.LogSearchService;
import com.iso.logus.domain.log.service.LogService;
import com.iso.logus.global.dto.CustomPageRequest;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/log")
public class LogController {

	private final LogService logService;
	private final LogSearchService logSearchService;
	
	@GetMapping(value = "/{logId}")
	@ApiImplicitParams({@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")})
	public LogDto.Response findLog(@PathVariable long logId) {
		return new LogDto.Response(logSearchService.findLogById(logId));
	}
	
	@GetMapping(value = "/page/{teamId}")
	@ApiImplicitParams({@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")})
	public Page<LogDto.Response> findPageLog(@PathVariable long teamId, @Valid CustomPageRequest pageRequest) {
		return logSearchService.findPageLogByTeamId(teamId, pageRequest.of()).map(LogDto.Response::new);
	}
	
	@GetMapping(value = "/find/{type}/{teamId}/{keyword}")
	@ApiImplicitParams({@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")})
	public Page<LogDto.Response> findPageLogByKeyword(@PathVariable KeywordType type, @PathVariable long teamId, @PathVariable String keyword, @Valid CustomPageRequest pageRequest) {
		return logSearchService.findPageLogByTeamIdAndKeyword(teamId, type, keyword, pageRequest.of()).map(LogDto.Response::new);
	}
	
	@PostMapping
	@ApiImplicitParams({@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")})
	@ResponseStatus(value = HttpStatus.CREATED)
	public void writeLog(@Valid @RequestBody LogDto.LogCreateRequest createRequest) {
		logService.writeLog(createRequest);
	}
	
	@PatchMapping
	@ApiImplicitParams({@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")})
	public void changeLog(@Valid @RequestBody LogDto.ChangeRequest changeRequest) {
		logService.changeLog(changeRequest);
	}
	
	@DeleteMapping(value = "/{logId}")
	@ApiImplicitParams({@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")})
	public void deleteLog(@PathVariable long logId) {
		logService.deleteLog(logId);
	}
}
