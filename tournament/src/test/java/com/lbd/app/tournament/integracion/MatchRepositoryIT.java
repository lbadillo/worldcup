package com.lbd.app.tournament.integracion;


import com.lbd.app.tournament.model.Group;
import com.lbd.app.tournament.model.Match;
import com.lbd.app.tournament.model.Result;
import com.lbd.app.tournament.model.Stage;
import com.lbd.app.tournament.model.Team;
import com.lbd.app.tournament.repository.MatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("integration")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MatchRepositoryIT {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MatchRepository matchRepository;

    private Group groupSaved;
    private Stage stageSaved;

    @BeforeEach
    void setUp() {
        Team team = Team.builder().name("Brazil").flag("br.png").wins(3).draws(1).losses(0).points(10).build();
        Team team2 = Team.builder().name("Argentina").flag("ar.png").wins(2).draws(2).losses(0).points(8).build();
        Team team3 =
                Team.builder().name("Venezuela").flag("ar.png").wins(2).draws(2).losses(0).points(8).build();
        Team team4 =
                Team.builder().name("Venezuela").flag("ar.png").wins(2).draws(2).losses(0).points(8).build();
        Group group = Group.builder().name("A").build();

        group.setTeams(Set.of(team2, team));

        Group group2 = Group.builder().name("B").build();
        group2.setTeams(Set.of(team3, team4));

        Stage stage = Stage.builder().name("Group Stage").build();


        Match match1 =
                Match.builder().group(group).team1(team).team2(team2).stage(stage).dateMatch(Instant.now()).build();

        Match match2 =
                Match.builder().team1(team).team2(team2).stage(stage).dateMatch(Instant.now()).build();

        Result result1 =
                Result.builder().match(match1).value1(2).value2(1).build();
        Result result2 =
                Result.builder().match(match2).value1(1).value2(1).build();

        match1.setResult(result1);
        match2.setResult(result2);

        entityManager.persistAndFlush(team);
        entityManager.persistAndFlush(team2);
        entityManager.persistAndFlush(team3);
        entityManager.persistAndFlush(team4);
        groupSaved = entityManager.persistAndFlush(group);
        entityManager.persistAndFlush(group2);
        stageSaved = entityManager.persistAndFlush(stage);

        entityManager.persistAndFlush(match1);
        entityManager.persistAndFlush(match2);

    }

    @Test
    void findByGroupAndStageWithResultOk() {

        List<Match> res = matchRepository.findByGroupAndStageWithResult(groupSaved.getId(),
                stageSaved.getId());
        assertEquals(1, res.size());
    }

    @Test
    void findByStageWithResultOk() {

        List<Match> res = matchRepository.findByStageWithResult(stageSaved.getId());
        assertEquals(2, res.size());
        assertEquals(1,
                res.stream().filter(match -> Objects.isNull(match.getGroup())).count());

    }
}