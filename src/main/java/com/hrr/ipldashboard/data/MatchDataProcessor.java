package com.hrr.ipldashboard.data;

import com.hrr.ipldashboard.model.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;

public class MatchDataProcessor implements ItemProcessor<MatchInput, Match> {
    private static final Logger logger = LoggerFactory.getLogger(MatchDataProcessor.class);

    @Override
    public Match process(final MatchInput matchInput) {
        Match match = new Match();
        match.setId(Long.parseLong(matchInput.getId()));
        match.setCity(matchInput.getCity());
        match.setDate(LocalDate.parse(matchInput.getDate()));
        match.setPlayerOfMatch(matchInput.getPlayer_of_match());
        match.setVenue(matchInput.getVenue());
        String firstTeam, secondTeam;
        // first team --> starts with bat
        if ("bat".equals(matchInput.getToss_decision())) {
            firstTeam = matchInput.getToss_winner();
            secondTeam = firstTeam.equals(matchInput.getTeam1()) ? matchInput.getTeam2() : matchInput.getTeam1();
        } else {
            secondTeam = matchInput.getToss_winner();
            firstTeam = secondTeam.equals(matchInput.getTeam1()) ? matchInput.getTeam2() : matchInput.getTeam1();
        }
        match.setTeam1(firstTeam);
        match.setTeam2(secondTeam);
        match.setTossWinner(matchInput.getToss_winner());
        match.setTossDecision(matchInput.getToss_decision());
        match.setWinner(matchInput.getWinner());
        match.setResult(matchInput.getResult());
        match.setResultMargin(matchInput.getResult_margin());
        match.setUmpire1(matchInput.getUmpire1());
        match.setUmpire2(matchInput.getUmpire2());
        return match;
    }
}
