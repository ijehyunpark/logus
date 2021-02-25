package com.iso.logus.domain.log.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LogRepository extends JpaRepository<Log, Long>{
	Page<Log> findAllByTeamId(long teamId, Pageable pageable);
	
	Page<Log> findAllByTeamIdAndTitleContaining(long teamId, String title, Pageable pageable);
	Page<Log> findAllByTeamIdAndContentContaining(long teamId, String content, Pageable pageable);
	Page<Log> findAllByTeamIdAndAuthorContaining(long teamId, String uid, Pageable pageable);
	
	@Query(value = "SELECT l FROM Log l WHERE l.team.id = :teamId AND (l.title = :keyword OR l.content = :keyword)")	
	Page<Log> findAllByTeamIdAndKeywordContaining(@Param("teamId") long teamId, @Param("keyword") String keyword, Pageable pageable);
}
