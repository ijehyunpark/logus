package com.iso.logus.domain.team.domain.teamauth;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iso.logus.domain.team.domain.team.Team;


public interface TeamAuthRepository extends JpaRepository<TeamAuth, Long>{
	List<TeamAuth> findByTeam(Team team);
	List<TeamAuth> findByTeamId(long id);
	Optional<TeamAuth> findByTeamIdAndName(long id, String name);
	void deleteByTeamIdAndName(long id, String name);
	
	boolean existsByTeamIdAndName(long id, String name);
	
	@Query(value = "SELECT t FROM TeamAuth t WHERE t.id = id AND t.type = type")
	Optional<TeamAuth> findByTeamIdAndTypeEqual(long id, int type);
}
