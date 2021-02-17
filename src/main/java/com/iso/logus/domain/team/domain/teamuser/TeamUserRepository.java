package com.iso.logus.domain.team.domain.teamuser;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamUserRepository extends JpaRepository<TeamUser, Long>{
	
	List<TeamUser> findAllByTeamId(long team_id);
	
	List<TeamUser> findAllByUserUid(String uid);
	
	Optional<TeamUser> findByTeamIdAndUserUid(long team_id, String uid);
	
	void deleteByTeamIdAndUserUid(long team_id, String uid);
}
