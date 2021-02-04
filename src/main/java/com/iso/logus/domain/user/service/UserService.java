package com.iso.logus.domain.user.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iso.logus.domain.user.domain.User;
import com.iso.logus.domain.user.domain.UserRepository;
import com.iso.logus.domain.user.dto.UserDto;
import com.iso.logus.domain.user.exception.UidDuplicationException;
import com.iso.logus.domain.user.exception.UserNotFoundException;
import com.iso.logus.domain.user.exception.WrongPasswordException;
import com.iso.logus.global.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	private final JwtTokenProvider JwtTokenProvider;
	
	@Transactional(readOnly = true)
	public User findUserByUid(String uid) {
		return userRepository.findByUid(uid).orElseThrow(() -> new UserNotFoundException(uid));
	}
	
	@Transactional(readOnly = true)
	public UserDto.Response findByUid(String uid) {
		return new UserDto.Response(findUserByUid(uid));
	}
	
	@Transactional(readOnly = true)
	public boolean isExistedUid(String uid) {
		return userRepository.existsByUid(uid);
	}
	
	public void SignUp(UserDto.SignUpRequest signUpRequest) {
		if(isExistedUid(signUpRequest.getUid()))
			throw new UidDuplicationException(signUpRequest.getUid());
		userRepository.save(signUpRequest.toEntity());
	}
	
	public void changeUserName(String uid, UserDto.ChangeNameRequest changeNameRequest) {
		User user = userRepository.findByUid(uid).orElseThrow(() -> new UserNotFoundException(uid));
		user.changeName(changeNameRequest);
	}
	
	public void deleteUserByUid(String uid) {
		User user = userRepository.findByUid(uid).orElseThrow(() -> new UserNotFoundException(uid));
		userRepository.delete(user);
	}
	
	public String signIn(UserDto.SignInRequest signInRequest) {
		User user = findUserByUid(signInRequest.getUid());
		if(!user.getPasswordDetail().isMatched(signInRequest.getPassword()))
			throw new WrongPasswordException();
		return JwtTokenProvider.createToken(user.getUid());
	}
	
		
	@Transactional(readOnly = true)
	public List<UserDto.Response> findByUserName(String name) {
		List<User> userList = userRepository.findByName(name);
		
		if(userList.isEmpty())
			throw new UserNotFoundException(null);
		
		List<UserDto.Response> dtoList = new ArrayList<>();
		for(User user : userList) 
			dtoList.add(new UserDto.Response(user));
		return dtoList;
	}
}
