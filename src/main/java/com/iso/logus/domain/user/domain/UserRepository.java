package com.iso.logus.domain.user.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String>{
	Optional<User> findByUid(String uid);
	
	List<User> findByName(String name);
	
	boolean existsByUid(String uid);
}