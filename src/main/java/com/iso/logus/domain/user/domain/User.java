package com.iso.logus.domain.user.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.iso.logus.domain.log.domain.Log;
import com.iso.logus.domain.team.domain.teamuser.TeamUser;
import com.iso.logus.domain.user.dto.UserDto;
import com.iso.logus.global.domain.TimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends TimeEntity implements UserDetails {
	@Id
	@Column(name = "uid", nullable = false)
	private String uid;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Embedded
	private Password passwordDetail;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<TeamUser> teamUsers = new HashSet<>();
	
	@OneToMany(mappedBy = "author", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	private Set<Log> logList = new HashSet<>();
	
	@Builder
	private User (String uid, String name, Password password){
		this.uid = uid;
		this.name = name;
		this.passwordDetail = password;
	}
	
	public void changeName(UserDto.ChangeNameRequest changeNameRequest) {
		this.name = changeNameRequest.getName();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getUsername() {
		return this.name;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.passwordDetail.getFailedCount() < 5;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getPassword() {
		return this.passwordDetail.getValue();
	}
}
