package com.iso.logus.domain.user.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String>{
	Optional<User> findByUid(String uid);
	
	boolean existsByUid(String uid);
}