package com.lbd.app.tournament.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

class GroupAndTeamDTOTest {

    @Test
    void shouldExposeRecordValues() {
        TeamDTO teamDTO = new TeamDTO(1L, "Brazil", "br.png");
        GroupDTO groupDTO = new GroupDTO(10L, "A", List.of(teamDTO));

        assertEquals(1L, teamDTO.id());
        assertEquals("Brazil", teamDTO.name());
        assertEquals("br.png", teamDTO.flag());
        assertEquals(10L, groupDTO.id());
        assertEquals("A", groupDTO.name());
        assertEquals(1, groupDTO.teams().size());
    }
}

