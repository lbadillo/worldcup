package com.lbd.app.tournament.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.lbd.app.tournament.dto.MatchDTO;
import com.lbd.app.tournament.dto.MatchResultDTO;
import com.lbd.app.tournament.dto.TeamSummaryDTO;
import com.lbd.app.tournament.service.MatchService;

class MatchesControllerTest {

    private static final MatchService MATCH_SERVICE_STUB = new MatchService() {
        @Override
        public List<MatchDTO> getMatchesByGroupAndStage(Long groupId, Long stageId) {
            return List.of(new MatchDTO(
                    100L,
                    groupId,
                    stageId,
                    "Group Stage",
                    new TeamSummaryDTO(1L, "Brazil", "br.png"),
                    new TeamSummaryDTO(2L, "Argentina", "ar.png"),
                    null,
                    new MatchResultDTO(2, 1)
            ));
        }
    };

    @Test
    void shouldReturnMatchesByGroupAndStage() {
        MatchesController controller = new MatchesController(MATCH_SERVICE_STUB);

        ResponseEntity<List<MatchDTO>> response = controller.getMatchesByGroupAndStage(10L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(100L, response.getBody().get(0).id());
        assertEquals(2, response.getBody().get(0).result().value1());
    }

    @Test
    void shouldReturnMatchesByStageWhenGroupIdIsNotSent() {
        MatchesController controller = new MatchesController(MATCH_SERVICE_STUB);

        ResponseEntity<List<MatchDTO>> response = controller.getMatchesByStage(1L, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertNull(response.getBody().get(0).groupId());
        assertEquals(1L, response.getBody().get(0).stageId());
    }
}

