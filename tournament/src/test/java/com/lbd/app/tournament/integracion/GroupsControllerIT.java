package com.lbd.app.tournament.integracion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        "INSERT INTO group_data (id, name) VALUES (10, 'A')",
        "INSERT INTO group_team (group_id, team_id) VALUES (10, 1)",
        "INSERT INTO group_team (group_id, team_id) VALUES (10, 2)"
})
class GroupsControllerIT {

    @LocalServerPort
    private int port;

    @Test
    void shouldReturnGroupsWithTeams() {
        RestClient restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();

        List<GroupDTO> response = restClient.get()
                .uri("/api/groups")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        assertNotNull(response);
        assertFalse(response.isEmpty());

        GroupDTO groupDTO = response.get(0);

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
}


