package com.iso.logus.domain.user.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.iso.logus.domain.user.exception.PasswordFailedExceededException;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password {
	@Column(name = "password", nullable = false)
	private String value;
	
	@Column(name = "password_failed_count", nullable = false)
	private int failedCount;
	
	@Builder
	private Password(final String value) {
		this.value = encodePassword(value);
		resetFailedCount();
	}
	
	private String encodePassword(final String password) {
		return new BCryptPasswordEncoder().encode(password);
	}
	
	private void updateFailedCount(boolean matches) {
		if(matches)
			resetFailedCount();
		else
			increaseFailedCount();
	}
	private void resetFailedCount() {
		this.failedCount = 0;
	}
	
	private void increaseFailedCount() {
		this.failedCount++;
	}
	
	public boolean isMatched(final String rawPassword) {
		if(failedCount >= 5)
			throw new PasswordFailedExceededException();
		
		final boolean matches = isMatches(rawPassword);
		updateFailedCount(matches);
		return matches;
	}
	
	public boolean isMatches(String rawPassword) {
		return new BCryptPasswordEncoder().matches(rawPassword, this.value);
	}
	
	public void changePassword(final String newPassword, final String oldPassword) {
		if(isMatched(oldPassword))
			this.value = encodePassword(newPassword);
	}
}
