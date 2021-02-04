package com.iso.logus.domain.user.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import com.iso.logus.domain.user.dto.UserDto;
import com.iso.logus.domain.user.service.UserService;
import com.iso.logus.global.exception.AccessDeniedException;
import com.iso.logus.global.jwt.JwtTokenProvider;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/user")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	private final JwtTokenProvider jwtTokenProvider;
	
	@GetMapping(value = "/{uid}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")
	})
	public UserDto.Response findUserByUid(@PathVariable String uid) {
		return userService.findByUid(uid);
	}
	
	@PostMapping(value = "/join")
	@ResponseStatus(value = HttpStatus.CREATED)
	public void SignUp(@RequestBody UserDto.SignUpRequest signUpRequest) {
		userService.SignUp(signUpRequest);
	}
	
	@PatchMapping(value = "/{uid}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")
	})
	public void changeUserName(@PathVariable String uid, @RequestBody UserDto.ChangeNameRequest changeNameRequest, HttpServletRequest request) {
		if(!jwtTokenProvider.validateUser(uid, request))
			throw new AccessDeniedException();
		userService.changeUserName(uid, changeNameRequest);
	}
	
	@DeleteMapping(value = "/{uid}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")
	})
	public void deleteUserByUid(@PathVariable String uid, HttpServletRequest request) {		
		if(!jwtTokenProvider.validateUser(uid, request))
			throw new AccessDeniedException();
		userService.deleteUserByUid(uid);
	}
	
	@PostMapping(value = "/login")
	public String signIn(@RequestBody UserDto.SignInRequest signInRequest) {
		return userService.signIn(signInRequest);
	}
	
	@GetMapping(value ="/find/{name}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")
	})
	public List<UserDto.Response> findUserByUserName(@PathVariable String name) {
		return userService.findByUserName(name);
	}
}
