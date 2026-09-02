package com.lbd.app.tournament.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.lbd.app.tournament.dto.MatchDTO;
import com.lbd.app.tournament.dto.MatchResultDTO;
import com.lbd.app.tournament.dto.TeamSummaryDTO;
import com.lbd.app.tournament.service.MatchService;

@ExtendWith(MockitoExtension.class)
public class MatchesControllerTest {
    @InjectMocks
    private MatchesController matchesController;

    @Mock
    private MatchService matchService;


    @Test
    void shouldReturnMatchesByGroupAndStage() {

        when(matchService.getMatchesByGroupAndStage(10L, 1L))
                .thenReturn(getMatches(10L, 1L));

        ResponseEntity<List<MatchDTO>> response =
                matchesController.getMatchesByGroupAndStage(10L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(100L, response.getBody().get(0).id());
        assertEquals(2, response.getBody().get(0).result().value1());
    }

    @Test
    void shouldReturnMatchesByStageWhenGroupIdIsNotSent() {
        when(matchService.getMatchesByGroupAndStage(null, 2L))
                .thenReturn(getMatches(null, 2L));

        ResponseEntity<List<MatchDTO>> response =
                matchesController.getMatchesByStage(2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertNull(response.getBody().get(0).groupId());
        assertEquals(2L, response.getBody().get(0).stageId());
    }

    private List<MatchDTO> getMatches(Long groupId,
                                      Long stageId) {
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
}

