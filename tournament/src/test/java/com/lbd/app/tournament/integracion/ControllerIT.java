package com.lbd.app.tournament.integracion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestClient;

import com.lbd.app.tournament.dto.GroupDTO;
import com.lbd.app.tournament.dto.ErrorResponseDTO;
import com.lbd.app.tournament.dto.MatchDTO;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
@Sql(statements = {
        "DELETE FROM group_team",
        "DELETE FROM bet",
        "DELETE FROM result",
        "DELETE FROM match_team",
        "DELETE FROM group_data",
        "DELETE FROM stage",
        "DELETE FROM team",
        "INSERT INTO team (id, name, flag, wins, draws, losses, points) VALUES (1, 'Brazil', 'br.png', 3, 1, 0, 10)",
        "INSERT INTO team (id, name, flag, wins, draws, losses, points) VALUES (2, 'Argentina', 'ar.png', 2, 2, 0, 8)",
        "INSERT INTO team (id, name, flag, wins, draws, losses, points) VALUES (3, 'Spain', 'es.png', 2, 1, 1, 7)",
        "INSERT INTO group_data (id, name) VALUES (10, 'A')",
        "INSERT INTO group_data (id, name) VALUES (11, 'B')",
        "INSERT INTO group_team (group_id, team_id) VALUES (10, 1)",
        "INSERT INTO group_team (group_id, team_id) VALUES (10, 2)",
        "INSERT INTO group_team (group_id, team_id) VALUES (11, 3)",
        "INSERT INTO stage (id, name) VALUES (1, 'Group Stage')",
        "INSERT INTO match_team (id, group_id, stage_id, team_1_id, team_2_id, date_match) VALUES (100, 10, 1, 1, 2, '2026-06-10 15:30:00')",
        "INSERT INTO match_team (id, group_id, stage_id, team_1_id, team_2_id, date_match) VALUES (101, 10, 1, 2, 1, '2026-06-11 15:30:00')",
        "INSERT INTO match_team (id, group_id, stage_id, team_1_id, team_2_id, date_match) VALUES (102, 11, 1, 3, 1, '2026-06-12 15:30:00')",
        "INSERT INTO result (id, match_id, value_1, value_2) VALUES (200, 100, 2, 1)"
})
class ControllerIT {

    @LocalServerPort
    private int port;

    @Test
    void shouldReturnGroupsWithTeams() {
        RestClient restClient = getRestClient();

        List<GroupDTO> response = restClient.get()
                .uri("/api/groups")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        assertNotNull(response);
        assertFalse(response.isEmpty());

        GroupDTO groupDTO = response.stream()
                .filter(group -> group.id() == 10L)
                .findFirst()
                .orElseThrow();

        assertEquals(10L, groupDTO.id());
        assertEquals("A", groupDTO.name());
        assertEquals(1L, groupDTO.teams().get(0).id());
        assertEquals("Brazil", groupDTO.teams().get(0).name());
        assertEquals("br.png", groupDTO.teams().get(0).flag());
        assertEquals(3, groupDTO.teams().get(0).wins());
        assertEquals(1, groupDTO.teams().get(0).draws());
        assertEquals(0, groupDTO.teams().get(0).losses());
        assertEquals(10, groupDTO.teams().get(0).points());
        assertEquals(2L, groupDTO.teams().get(1).id());
        assertEquals("Argentina", groupDTO.teams().get(1).name());
        assertEquals(8, groupDTO.teams().get(1).points());
    }

    @Test
    void shouldReturnMatchesByGroupAndStage() {
        RestClient restClient = getRestClient();

        List<MatchDTO> response = restClient.get()
                .uri("/api/matches/groups/10/stages/1")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(100L, response.get(0).id());
        assertEquals(10L, response.get(0).groupId());
        assertEquals(1L, response.get(0).stageId());
        assertEquals("Group Stage", response.get(0).stageName());
        assertEquals("Brazil", response.get(0).team1().name());
        assertEquals("Argentina", response.get(0).team2().name());
        assertEquals(2, response.get(0).result().value1());
        assertEquals(1, response.get(0).result().value2());

        assertEquals(101L, response.get(1).id());
        assertEquals(0, response.get(1).result().value1());
        assertEquals(0, response.get(1).result().value2());
    }

    @Test
    void shouldReturnMatchesByStageWhenGroupIdIsNotSent() {
        RestClient restClient = getRestClient();

        List<MatchDTO> response = restClient.get()
                .uri("/api/matches/stages/1")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(3, response.size());
        assertEquals(100L, response.get(0).id());
        assertEquals(10L, response.get(0).groupId());
        assertEquals(101L, response.get(1).id());
        assertEquals(10L, response.get(1).groupId());
        assertEquals(102L, response.get(2).id());
        assertEquals(11L, response.get(2).groupId());
        assertEquals(0, response.get(1).result().value1());
        assertEquals(0, response.get(2).result().value1());
    }

    @Test
    void shouldReturnNotFoundErrorWhenStageDoesNotExist() {
        RestClient restClient = getRestClient();

        ErrorResponseDTO response = restClient.get()
                .uri("/api/matches/stages/99")
                .retrieve()
                .onStatus(status -> status.value() == 404, (request, clientResponse) -> {
                })
                .body(ErrorResponseDTO.class);

        assertNotNull(response);
        assertEquals(404, response.status());
        assertEquals("Not Found", response.error());
        assertTrue(response.message().contains("Stage not found with id: 99"));
        assertEquals("/api/matches/stages/99", response.path());
    }

    private RestClient getRestClient() {
        return RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();

    }
}


