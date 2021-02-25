package com.iso.logus.domain.team.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iso.logus.domain.team.domain.team.Team;
import com.iso.logus.domain.team.domain.team.TeamRepository;
import com.iso.logus.domain.team.dto.TeamDto;
import com.iso.logus.domain.team.dto.TeamUserDto;
import com.iso.logus.domain.team.dto.TeamUserDto.ChangeAuthRequest;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamService {

	private final TeamSearchService teamSearchService;
	private final TeamRepository teamRepository;
	private final TeamAuthService teamAuthService;
	private final TeamUserService teamUserService;
	

	
	public Team createTeam(TeamDto.CreateRequest createRequest, String masterUserUid) {		
		Team team = teamRepository.save(createRequest.toEntity());
		teamAuthService.setUpTeamAuth(team);
		
		TeamUserDto.JoinRequest joinRequest = buildTeamCreatorJoinRequest(masterUserUid, team.getId(), createRequest.getCustomName());
		teamUserService.joinNewMember(joinRequest);
		
		TeamUserDto.ChangeAuthRequest changeAuthRequest = buildMasterAuthChangeRequest(masterUserUid, team.getId());
		teamUserService.changeAuth(changeAuthRequest);
		
		return team;
	}

	public Team updateTeam(long id, TeamDto.UpdateRequest updateRequest) {
		Team team = teamSearchService.findTeamById(id);
		team.update(updateRequest);
		return team;
	}
	
	public void deleteTeam(long id) {
		Team team = teamSearchService.findTeamById(id);
		teamRepository.delete(team);
	}
	
	private TeamUserDto.JoinRequest buildTeamCreatorJoinRequest(String masterUserUid, long teamId, String customName){
		return TeamUserDto.JoinRequest.builder()
				.teamId(teamId)
				.uid(masterUserUid)
				.customName(customName)
				.build();
	}
	
	private TeamUserDto.ChangeAuthRequest buildMasterAuthChangeRequest(String masterUserUid, long teamId) {
		return ChangeAuthRequest.builder()
				.teamAuthName(TeamAuthBaseData.getMasterAuthName())
				.uid(masterUserUid)
				.teamId(teamId)
				.build();
	}
}
