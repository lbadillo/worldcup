package com.lbd.app.tournament.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.Set;

import org.junit.jupiter.api.Test;

class ModelEntitiesTest {

    @Test
    void shouldCreateTeamAndStageWithBuilder() {
        Team team = Team.builder().id(1L).name("Brazil").flag("br.png").wins(3).draws(1).losses(0).points(10).build();
        Stage stage = Stage.builder().id(1L).name("Group Stage").build();

        assertEquals(1L, team.getId());
        assertEquals("Brazil", team.getName());
        assertEquals("br.png", team.getFlag());
        assertEquals(3, team.getWins());
        assertEquals(1, team.getDraws());
        assertEquals(0, team.getLosses());
        assertEquals(10, team.getPoints());
        assertEquals("Group Stage", stage.getName());
    }

    @Test
    void shouldCreateGroupAndTeamsRelation() {
        Team team = Team.builder().id(1L).name("Brazil").flag("br.png").wins(3).draws(1).losses(0).points(10).build();
        Group group = Group.builder().id(10L).name("A").build();

        group.setTeams(Set.of(team));

        assertEquals(10L, group.getId());
        assertEquals("A", group.getName());
        assertEquals(1, group.getTeams().size());
    }

    @Test
    void shouldCreateMatchResultAndBet() {
        Group group = Group.builder().id(10L).name("A").build();
        Team team1 = Team.builder().id(1L).name("Brazil").flag("br.png").wins(3).draws(1).losses(0).points(10).build();
        Team team2 = Team.builder().id(2L).name("Argentina").flag("ar.png").wins(2).draws(0).losses(2).points(6).build();
        Stage stage = Stage.builder().id(1L).name("Group Stage").build();

        Instant dateMatch = Instant.parse("2026-08-31T21:30:00Z");
        Match match = Match.builder()
                .id(100L)
                .groupEntity(group)
                .stage(stage)
                .team1(team1)
                .team2(team2)
                .dateMatch(dateMatch)
                .build();

        Result result = Result.builder().id(200L).match(match).value1(2).value2(1).build();

        User user = User.builder()
                .id(300L)
                .name("Luis")
                .email("luis@mail.com")
                .providerId("google")
                .providerUserId("lucho")
                .build();
        Bet bet = Bet.builder().id(400L).user(user).match(match).value1(2).value2(1).points(3).build();

        assertEquals(dateMatch, match.getDateMatch());
        assertEquals(2, result.getValue1());
        assertEquals(1, result.getValue2());
        assertEquals("lucho", user.getProviderUserId());
        assertEquals(3, bet.getPoints());
        assertEquals(3, bet.getMatch().getTeam1().getWins());
        assertEquals(2, bet.getMatch().getTeam2().getWins());
        assertEquals(10, bet.getMatch().getTeam1().getPoints());
        assertEquals(6, bet.getMatch().getTeam2().getPoints());
        assertNotNull(bet.getMatch());
        assertTrue(bet.getMatch().getTeam1().getName().contains("Brazil"));
    }
}
