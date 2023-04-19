package com.hrr.ipldashboard.data;

import com.hrr.ipldashboard.model.Match;
import com.hrr.ipldashboard.model.Team;
import com.hrr.ipldashboard.repository.MatchRepository;
import com.hrr.ipldashboard.repository.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {
    private static final Logger logger = LoggerFactory.getLogger(JobCompletionNotificationListener.class);
    private final JdbcTemplate jdbcTemplate;
    private final EntityManager em;
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate, EntityManager em, MatchRepository matchRepository, TeamRepository teamRepository) {
        logger.info("Instantiating a JobCompletionNotificationListener ... - " + this.hashCode());
        this.jdbcTemplate = jdbcTemplate;
        this.em = em;
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
    }

    @Override
//    @Transactional -- I am not using EntityManager, so I don't need a transaction to persist data
    public void afterJob(JobExecution jobExecution) {
        logger.info("Job (" + jobExecution.getJobInstance().getJobName() + ") Status: " + jobExecution.getStatus());
        if (BatchStatus.COMPLETED == jobExecution.getStatus()) {
            // method 1
            logger.info("All Matches queried via JDBC:");
            jdbcTemplate.query("SELECT team1, team2, date FROM match",
                            (rs, rowNum) -> "Team 1: " + rs.getString(1) + ", Team 2: " + rs.getString(2) + ", Date: " + rs.getString(3))
                    .forEach(logger::info);
            logger.info("--------------------");

            // method 2
            logger.info("All Matches queried via JPA Repository:");
            matchRepository.findAll().forEach(match -> logger.info(match.toString()));
            logger.info("--------------------");

            Set<Team> allTeamsSet = new HashSet<>();
            List<Match> allMatchesList = matchRepository.findAll();
            Set<String> allTeamsNameSet = new HashSet<>();
            allTeamsNameSet.addAll(allMatchesList.stream().map(Match::getTeam1).collect(Collectors.toSet()));
            allTeamsNameSet.addAll(allMatchesList.stream().map(Match::getTeam2).collect(Collectors.toSet()));
            for (String teamName : allTeamsNameSet) {
                Team team = new Team();
                team.setTeamName(teamName);
                team.setTotalMatches(allMatchesList.stream()
                        .filter(match -> match.getTeam1().equals(teamName) || match.getTeam2().equals(teamName))
                        .count());
                team.setTotalWins(allMatchesList.stream()
                        .filter(match -> match.getWinner().equals(teamName))
                        .count());
                allTeamsSet.add(team);
            }
            logger.info("All Teams created before persisting:");
            allTeamsSet.forEach(team -> logger.info(team.toString()));
            logger.info("--------------------");
            teamRepository.saveAll(allTeamsSet);
            logger.info("All Teams created after persisting:");
            teamRepository.findAll().forEach(team -> logger.info(team.toString()));
            logger.info("--------------------");
        }
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        logger.info("About to start the batch job: " + jobExecution.getJobInstance().getJobName());
    }

}
