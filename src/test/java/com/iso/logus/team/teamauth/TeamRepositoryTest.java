package com.iso.logus.team.teamauth;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.util.Pair;
import org.springframework.test.context.ActiveProfiles;

import com.iso.logus.domain.team.domain.team.Team;
import com.iso.logus.domain.team.domain.team.TeamRepository;
import com.iso.logus.domain.team.domain.teamauth.TeamAuth;
import com.iso.logus.domain.team.domain.teamauth.TeamAuthRepository;
import com.iso.logus.team.TeamTestSampleData;

@DataJpaTest
@Disabled
@ActiveProfiles("test")
public class TeamRepositoryTest {

	@Autowired
	private TeamRepository teamRepository;
	
	@Autowired
	private TeamAuthRepository teamAuthRepository;
	
	private final TeamTestSampleData sampleData = new TeamTestSampleData();
	
	@Test
	public void findByTeamIdTest() {
		//given
		final Pair<Team, TeamAuth> TeamAuthData = saveTeamAuth();
		final Team team = TeamAuthData.getFirst();
		
		//when
		List<TeamAuth> result = teamAuthRepository.findByTeamId(team.getId());
		
		//then
		assertNotNull(result);
		assertThat(result.size(), is(1));
	}
	
	@Test
	public void findByTeamIdAndName() {
		//given
		final Pair<Team, TeamAuth> TeamAuthData = saveTeamAuth();
		final Team team = TeamAuthData.getFirst();
		final TeamAuth teamAuth = TeamAuthData.getSecond();
		
		//when
		Optional<TeamAuth> result = teamAuthRepository.findByTeamIdAndName(team.getId(), teamAuth.getName());
		
		//then
		assertTrue(result.isPresent());
		TeamAuth value = result.orElseThrow(RuntimeException::new);
		assertThat(value.getName(), is(teamAuth.getName()));
	}
	
	public Pair<Team, TeamAuth> saveTeamAuth() {
		Team team = sampleData.makeTeam();
		teamRepository.save(team);
		TeamAuth teamAuth = sampleData.returnSampleAuth(team);
		teamAuthRepository.save(teamAuth);
		return Pair.of(team, teamAuth);
	}
}
