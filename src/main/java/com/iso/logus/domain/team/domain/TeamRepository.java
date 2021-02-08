package com.iso.logus.domain.team.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long>{

	List<Team> findAllByName(String name);
}
