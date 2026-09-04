package com.lbd.app.tournament.controller;

import com.lbd.app.tournament.dto.GroupDTO;
import com.lbd.app.tournament.dto.TeamDTO;
import com.lbd.app.tournament.service.GroupService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GroupsControllerTest {
    @Mock
    private GroupService groupService;

    @InjectMocks
    private GroupsController groupsController;


    @Test
    void shouldReturnGroupsFromService() {
        when(groupService.getAllGroupsWithTeams()).thenReturn(List.of(
                new GroupDTO(10L, "A", List.of(
                        new TeamDTO(1L, "Brazil", "br.png", 3, 1, 0, 10)))));

        ResponseEntity<List<GroupDTO>> response = groupsController.getAllGroups();

        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    //TODO delete this method when the security is implemented
    @Test
    void shouldReturnTestString() {
        ResponseEntity<String> response = groupsController.getTest();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}

