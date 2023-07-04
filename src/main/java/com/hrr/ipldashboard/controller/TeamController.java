package com.hrr.ipldashboard.controller;

import com.hrr.ipldashboard.model.Team;
import com.hrr.ipldashboard.repository.MatchRepository;
import com.hrr.ipldashboard.repository.TeamRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TeamController {

    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;

    public TeamController(TeamRepository teamRepository, MatchRepository matchRepository) {
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
    }

    @GetMapping("/team/{teamName}")
    public Team getTeam(@PathVariable String teamName) {
        Team team = teamRepository.findByTeamName(teamName);
        team.setMatchesList(matchRepository.findLatestMatches(teamName, 4));
        return team;
    }

    @GetMapping("/teams")
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }
}
