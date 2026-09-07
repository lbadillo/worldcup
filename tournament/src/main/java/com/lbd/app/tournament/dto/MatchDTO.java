package com.lbd.app.tournament.dto;

import java.time.Instant;
import java.util.List;

public record MatchDTO(
        Long id,
        Long groupId,
        Long stageId,
        String stageName,
        TeamSummaryDTO team1,
        TeamSummaryDTO team2,
        Instant dateMatch,
        MatchValueDTO result

) {
}

