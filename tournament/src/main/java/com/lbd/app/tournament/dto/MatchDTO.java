package com.lbd.app.tournament.dto;

import java.time.Instant;

public record MatchDTO(
        Long id,
        Long groupId,
        Long stageId,
        String stageName,
        TeamSummaryDTO team1,
        TeamSummaryDTO team2,
        Instant dateMatch,
        MatchResultDTO result
) {
}

