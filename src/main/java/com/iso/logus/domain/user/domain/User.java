package com.iso.logus.domain.user.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.iso.logus.domain.user.dto.UserDto;
import com.iso.logus.global.domain.TimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends TimeEntity {
	@Id
	@Column(name = "uid", nullable = false)
	private String uid;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Embedded
	private Password password;
	
	@Builder
	public User (String uid, String name, Password password){
		this.uid = uid;
		this.name = name;
		this.password = password;
	}
	
	public void changeName(UserDto.ChangeNameRequest changeNameRequest) {
		this.name = changeNameRequest.getName();
	}
}
