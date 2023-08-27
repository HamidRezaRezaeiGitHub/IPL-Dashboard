package com.hrr.ipldashboard.controller;

import com.hrr.ipldashboard.model.Match;
import com.hrr.ipldashboard.model.Team;
import com.hrr.ipldashboard.repository.MatchRepository;
import com.hrr.ipldashboard.repository.TeamRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
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

    @GetMapping("/team/{teamName}/matches")
    public List<Match> getMatchesForTeam(@PathVariable String teamName, @RequestParam int year) {
        return matchRepository.getMatchesByTeamByYear(teamName, year);
    }

    @GetMapping("/team")
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }
}
