package com.lbd.app.tournament.dto;

public record UserBetDTO(Long matchId,
                         String team1Name,
                         Integer team1Result,
                         Integer team1Bet,
                         String team2Name,
                         Integer team2Result,
                         Integer team2Bet,
                         Long groupId,
                         String groupName,
                         String stageName,
                         Long stageId,
                         Long userId,
                         Integer points) {
}
