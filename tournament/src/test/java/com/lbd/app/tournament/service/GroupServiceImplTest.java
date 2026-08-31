package com.lbd.app.tournament.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.lbd.app.tournament.dto.GroupDTO;
import com.lbd.app.tournament.model.Group;
import com.lbd.app.tournament.model.Team;
import com.lbd.app.tournament.repository.GroupRepository;
import com.lbd.app.tournament.service.impl.GroupServiceImpl;

class GroupServiceImplTest {

    @Test
    void shouldMapGroupsToDtos() {
        Team team = Team.builder().id(1L).name("Brazil").flag("br.png").build();
        Group group = Group.builder().id(10L).name("A").build();
        group.setTeams(Set.of(team));

        GroupRepository repository = (GroupRepository) Proxy.newProxyInstance(
                GroupRepository.class.getClassLoader(),
                new Class<?>[]{GroupRepository.class},
                (proxy, method, args) -> {
                    if ("findAllWithTeams".equals(method.getName())) {
                        return List.of(group);
                    }
                    if ("toString".equals(method.getName())) {
                        return "GroupRepositoryProxy";
                    }
                    throw new UnsupportedOperationException("Method not supported in this test: " + method.getName());
                }
        );

        GroupService service = new GroupServiceImpl(repository);
        List<GroupDTO> response = service.getAllGroupsWithTeams();

        assertEquals(1, response.size());
        assertEquals(10L, response.get(0).id());
        assertEquals("A", response.get(0).name());
        assertEquals(1L, response.get(0).teams().get(0).id());
    }
}

