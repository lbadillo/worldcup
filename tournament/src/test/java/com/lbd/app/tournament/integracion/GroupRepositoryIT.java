package com.lbd.app.tournament.integracion;


import com.lbd.app.tournament.model.Group;
import com.lbd.app.tournament.model.Team;
import com.lbd.app.tournament.repository.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("integration")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GroupRepositoryIT {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GroupRepository groupRepository;


    @BeforeEach
    void setUp() {
        Team team = Team.builder().name("Brazil").flag("br.png").wins(3).draws(1).losses(0).points(10).build();
        Team team2 = Team.builder().name("Argentina").flag("ar.png").wins(2).draws(2).losses(0).points(8).build();
        Team team3 =
                Team.builder().name("Venezuela").flag("ar.png").wins(2).draws(2).losses(0).points(8).build();
        Group group = Group.builder().name("A").build();
        group.setTeams(Set.of(team2, team));


        Group group2 = Group.builder().name("B").build();
        group2.setTeams(Set.of(team3));
        entityManager.persistAndFlush(team);
        entityManager.persistAndFlush(team2);
        entityManager.persistAndFlush(team3);
        entityManager.persistAndFlush(group);
        entityManager.persistAndFlush(group2);
    }

    @Test
    void shouldFindAllGroupsWithTeams() {

        List<Group> res = groupRepository.findAllWithTeams();
        assertEquals(2, res.size());
    }
}