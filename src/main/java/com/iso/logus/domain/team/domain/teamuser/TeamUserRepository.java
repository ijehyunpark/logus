package com.iso.logus.domain.team.domain.teamuser;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamUserRepository extends JpaRepository<TeamUser, Long>{
	
	List<TeamUser> findAllByTeamId(long teamId);
	
	List<TeamUser> findAllByUserUid(String uid);
	
	Optional<TeamUser> findByTeamIdAndUserUid(long teamId, String uid);
	
	void deleteByTeamIdAndUserUid(long teamId, String uid);
	
	@Query(value = "SELECT u FROM TeamUser u WHERE u.id = (SELECT u.id FROM TeamUser u LEFT JOIN TeamAuth a WHERE u.team.id = :teamId AND a.type = :type)")
	List<TeamUser>findByTeamAuthType(@Param("teamId") long teamId, @Param("type") int type);
}
