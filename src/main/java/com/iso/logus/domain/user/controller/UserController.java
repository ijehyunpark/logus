package com.iso.logus.domain.user.controller;

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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/user")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	@GetMapping(value = "/{uid}")
	public UserDto.Response findUserByUid(@PathVariable String uid) {
		return new UserDto.Response(userService.findByUid(uid));
	}
	
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public void SignUp(@RequestBody UserDto.SingUpRequest singUpRequest) {
		userService.SignUp(singUpRequest);
	}
	
	@PatchMapping(value = "/{uid}")
	public void changeUserName(@PathVariable String uid, @RequestBody UserDto.ChangeNameRequest changeNameRequest) {
		userService.changeUserName(uid, changeNameRequest);
	}
	
	@DeleteMapping(value = "/{uid}")
	public void deleteUserByUid(@PathVariable String uid) {
		userService.deleteUserByUid(uid);
	}
}
