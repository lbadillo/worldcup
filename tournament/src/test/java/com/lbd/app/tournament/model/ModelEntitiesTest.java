package com.lbd.app.tournament.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.Test;

class ModelEntitiesTest {

    @Test
    void shouldCreateTeamAndStageWithBuilder() {
        Team team = Team.builder().id(1L).name("Brazil").flag("br.png").build();
        Stage stage = Stage.builder().id(1L).name("Group Stage").build();

        assertEquals(1L, team.getId());
        assertEquals("Brazil", team.getName());
        assertEquals("br.png", team.getFlag());
        assertEquals("Group Stage", stage.getName());
    }

    @Test
    void shouldCreateGroupAndTeamsRelation() {
        Team team = Team.builder().id(1L).name("Brazil").flag("br.png").build();
        Group group = Group.builder().id(10L).name("A").build();

        group.setTeams(Set.of(team));

        assertEquals(10L, group.getId());
        assertEquals("A", group.getName());
        assertEquals(1, group.getTeams().size());
    }

    @Test
    void shouldCreateMatchResultAndBet() {
        Group group = Group.builder().id(10L).name("A").build();
        Team team1 = Team.builder().id(1L).name("Brazil").flag("br.png").build();
        Team team2 = Team.builder().id(2L).name("Argentina").flag("ar.png").build();
        Stage stage = Stage.builder().id(1L).name("Group Stage").build();

        LocalDateTime dateMatch = LocalDateTime.of(2026, 6, 10, 15, 30);
        Match match = Match.builder()
                .id(100L)
                .groupEntity(group)
                .stage(stage)
                .team1(team1)
                .team2(team2)
                .dateMatch(dateMatch)
                .build();

        Result result = Result.builder().id(200L).match(match).value1(2).value2(1).build();

        User user = User.builder().id(300L).name("Luis").nickname("lucho").email("luis@mail.com").build();
        Bet bet = Bet.builder().id(400L).user(user).match(match).value1(2).value2(1).points(3).build();

        assertEquals(dateMatch, match.getDateMatch());
        assertEquals(2, result.getValue1());
        assertEquals(1, result.getValue2());
        assertEquals("lucho", user.getNickname());
        assertEquals(3, bet.getPoints());
        assertNotNull(bet.getMatch());
        assertTrue(bet.getMatch().getTeam1().getName().contains("Brazil"));
    }
}

