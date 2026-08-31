package com.lbd.app.tournament.integracion;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.lbd.app.tournament.dto.GroupDTO;
import com.lbd.app.tournament.service.GroupService;

@SpringBootTest
@ActiveProfiles("integration")
@Sql(statements = {
        "DELETE FROM group_team",
        "DELETE FROM bet",
        "DELETE FROM result",
        "DELETE FROM match_team",
        "DELETE FROM group_data",
        "DELETE FROM stage",
        "DELETE FROM team",
        "INSERT INTO team (id, name, flag) VALUES (1, 'Brazil', 'br.png')",
        "INSERT INTO group_data (id, name) VALUES (10, 'A')",
        "INSERT INTO group_team (group_id, team_id) VALUES (10, 1)"
})
class GroupsControllerIT {

    @Autowired
    private GroupService groupService;

    @Test
    void shouldReturnGroupsWithTeams() throws Exception {
        GroupDTO groupDTO = groupService.getAllGroupsWithTeams().get(0);

        assertEquals(10L, groupDTO.id());
        assertEquals("A", groupDTO.name());
        assertEquals(1L, groupDTO.teams().get(0).id());
        assertEquals("Brazil", groupDTO.teams().get(0).name());
        assertEquals("br.png", groupDTO.teams().get(0).flag());
    }
}


