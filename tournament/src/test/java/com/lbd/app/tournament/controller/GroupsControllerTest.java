package com.lbd.app.tournament.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.lbd.app.tournament.dto.GroupDTO;
import com.lbd.app.tournament.dto.TeamDTO;
import com.lbd.app.tournament.service.GroupService;

class GroupsControllerTest {

    @Test
    void shouldReturnGroupsFromService() {
        List<GroupDTO> expected = List.of(
                new GroupDTO(10L, "A", List.of(new TeamDTO(1L, "Brazil", "br.png")))
        );

        GroupService groupService = () -> expected;
        GroupsController controller = new GroupsController(groupService);

        ResponseEntity<List<GroupDTO>> response = controller.getAllGroups();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }
}

