package com.lbd.app.tournament.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Proxy;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.lbd.app.tournament.dto.MatchDTO;
import com.lbd.app.tournament.exception.ResourceNotFoundException;
import com.lbd.app.tournament.model.Group;
import com.lbd.app.tournament.model.Match;
import com.lbd.app.tournament.model.Result;
import com.lbd.app.tournament.model.Stage;
import com.lbd.app.tournament.model.Team;
import com.lbd.app.tournament.repository.GroupRepository;
import com.lbd.app.tournament.repository.MatchRepository;
import com.lbd.app.tournament.repository.StageRepository;
import com.lbd.app.tournament.service.impl.MatchServiceImpl;

class MatchServiceImplTest {

    @Test
    void shouldMapMatchesByGroupAndStageToDtos() {
        Group group = Group.builder().id(10L).name("A").build();
        Stage stage = Stage.builder().id(1L).name("Group Stage").build();
        Team team1 = Team.builder().id(1L).name("Brazil").flag("br.png").points(10).wins(3).draws(1).losses(0).build();
        Team team2 = Team.builder().id(2L).name("Argentina").flag("ar.png").points(8).wins(2).draws(2).losses(0).build();

        Match match = Match.builder()
                .id(100L)
                .groupEntity(group)
                .stage(stage)
                .team1(team1)
                .team2(team2)
                .dateMatch(Instant.parse("2026-06-10T15:30:00Z"))
                .build();
        match.setResult(Result.builder().id(200L).match(match).value1(2).value2(1).build());

        Match matchWithoutResult = Match.builder()
                .id(101L)
                .groupEntity(group)
                .stage(stage)
                .team1(team2)
                .team2(team1)
                .dateMatch(Instant.parse("2026-06-11T15:30:00Z"))
                .build();

        MatchRepository matchRepository = (MatchRepository) Proxy.newProxyInstance(
                MatchRepository.class.getClassLoader(),
                new Class<?>[]{MatchRepository.class},
                (proxy, method, args) -> {
                    if ("findByGroupAndStageWithResult".equals(method.getName())) {
                        return List.of(match, matchWithoutResult);
                    }
                    if ("toString".equals(method.getName())) {
                        return "MatchRepositoryProxy";
                    }
                    throw new UnsupportedOperationException("Method not supported in this test: " + method.getName());
                }
        );

        GroupRepository groupRepository = (GroupRepository) Proxy.newProxyInstance(
                GroupRepository.class.getClassLoader(),
                new Class<?>[]{GroupRepository.class},
                (proxy, method, args) -> {
                    if ("existsById".equals(method.getName())) {
                        return true;
                    }
                    if ("toString".equals(method.getName())) {
                        return "GroupRepositoryProxy";
                    }
                    throw new UnsupportedOperationException("Method not supported in this test: " + method.getName());
                }
        );

        StageRepository stageRepository = (StageRepository) Proxy.newProxyInstance(
                StageRepository.class.getClassLoader(),
                new Class<?>[]{StageRepository.class},
                (proxy, method, args) -> {
                    if ("existsById".equals(method.getName())) {
                        return true;
                    }
                    if ("toString".equals(method.getName())) {
                        return "StageRepositoryProxy";
                    }
                    throw new UnsupportedOperationException("Method not supported in this test: " + method.getName());
                }
        );

        MatchService service = new MatchServiceImpl(matchRepository, groupRepository, stageRepository);
        List<MatchDTO> response = service.getMatchesByGroupAndStage(10L, 1L);

        assertEquals(2, response.size());
        assertEquals(100L, response.get(0).id());
        assertEquals(10L, response.get(0).groupId());
        assertEquals(1L, response.get(0).stageId());
        assertEquals("Group Stage", response.get(0).stageName());
        assertEquals(2, response.get(0).result().value1());
        assertEquals(1, response.get(0).result().value2());
        assertEquals(101L, response.get(1).id());
        assertEquals(0, response.get(1).result().value1());
        assertEquals(0, response.get(1).result().value2());
    }

    @Test
    void shouldReturnAllMatchesByStageWhenGroupIdIsNull() {
        Group group1 = Group.builder().id(10L).name("A").build();
        Group group2 = Group.builder().id(11L).name("B").build();
        Stage stage = Stage.builder().id(1L).name("Group Stage").build();
        Team team1 = Team.builder().id(1L).name("Brazil").flag("br.png").points(10).wins(3).draws(1).losses(0).build();
        Team team2 = Team.builder().id(2L).name("Argentina").flag("ar.png").points(8).wins(2).draws(2).losses(0).build();

        Match match1 = Match.builder().id(100L).groupEntity(group1).stage(stage).team1(team1).team2(team2)
                .dateMatch(Instant.parse("2026-06-10T15:30:00Z")).build();
        Match match2 = Match.builder().id(102L).groupEntity(group2).stage(stage).team1(team2).team2(team1)
                .dateMatch(Instant.parse("2026-06-12T15:30:00Z")).build();

        MatchRepository matchRepository = (MatchRepository) Proxy.newProxyInstance(
                MatchRepository.class.getClassLoader(),
                new Class<?>[]{MatchRepository.class},
                (proxy, method, args) -> {
                    if ("findByStageWithResult".equals(method.getName())) {
                        return List.of(match1, match2);
                    }
                    if ("toString".equals(method.getName())) {
                        return "MatchRepositoryProxy";
                    }
                    throw new UnsupportedOperationException("Method not supported in this test: " + method.getName());
                }
        );

        GroupRepository groupRepository = (GroupRepository) Proxy.newProxyInstance(
                GroupRepository.class.getClassLoader(),
                new Class<?>[]{GroupRepository.class},
                (proxy, method, args) -> {
                    if ("toString".equals(method.getName())) {
                        return "GroupRepositoryProxy";
                    }
                    throw new UnsupportedOperationException("Method not supported in this test: " + method.getName());
                }
        );

        StageRepository stageRepository = (StageRepository) Proxy.newProxyInstance(
                StageRepository.class.getClassLoader(),
                new Class<?>[]{StageRepository.class},
                (proxy, method, args) -> {
                    if ("existsById".equals(method.getName())) {
                        return true;
                    }
                    if ("toString".equals(method.getName())) {
                        return "StageRepositoryProxy";
                    }
                    throw new UnsupportedOperationException("Method not supported in this test: " + method.getName());
                }
        );

        MatchService service = new MatchServiceImpl(matchRepository, groupRepository, stageRepository);
        List<MatchDTO> response = service.getMatchesByGroupAndStage(null, 1L);

        assertEquals(2, response.size());
        assertEquals(10L, response.get(0).groupId());
        assertEquals(11L, response.get(1).groupId());
        assertEquals(0, response.get(0).result().value1());
        assertEquals(0, response.get(0).result().value2());
    }

    @Test
    void shouldThrowWhenStageDoesNotExist() {
        MatchRepository matchRepository = (MatchRepository) Proxy.newProxyInstance(
                MatchRepository.class.getClassLoader(),
                new Class<?>[]{MatchRepository.class},
                (proxy, method, args) -> {
                    if ("toString".equals(method.getName())) {
                        return "MatchRepositoryProxy";
                    }
                    throw new UnsupportedOperationException("Method not supported in this test: " + method.getName());
                }
        );

        GroupRepository groupRepository = (GroupRepository) Proxy.newProxyInstance(
                GroupRepository.class.getClassLoader(),
                new Class<?>[]{GroupRepository.class},
                (proxy, method, args) -> true
        );

        StageRepository stageRepository = (StageRepository) Proxy.newProxyInstance(
                StageRepository.class.getClassLoader(),
                new Class<?>[]{StageRepository.class},
                (proxy, method, args) -> false
        );

        MatchService service = new MatchServiceImpl(matchRepository, groupRepository, stageRepository);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.getMatchesByGroupAndStage(10L, 99L));

        assertEquals("Stage not found with id: 99", ex.getMessage());
    }

    @Test
    void shouldThrowWhenGroupDoesNotExist() {
        MatchRepository matchRepository = (MatchRepository) Proxy.newProxyInstance(
                MatchRepository.class.getClassLoader(),
                new Class<?>[]{MatchRepository.class},
                (proxy, method, args) -> {
                    if ("toString".equals(method.getName())) {
                        return "MatchRepositoryProxy";
                    }
                    throw new UnsupportedOperationException("Method not supported in this test: " + method.getName());
                }
        );

        GroupRepository groupRepository = (GroupRepository) Proxy.newProxyInstance(
                GroupRepository.class.getClassLoader(),
                new Class<?>[]{GroupRepository.class},
                (proxy, method, args) -> false
        );

        StageRepository stageRepository = (StageRepository) Proxy.newProxyInstance(
                StageRepository.class.getClassLoader(),
                new Class<?>[]{StageRepository.class},
                (proxy, method, args) -> true
        );

        MatchService service = new MatchServiceImpl(matchRepository, groupRepository, stageRepository);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.getMatchesByGroupAndStage(50L, 1L));

        assertEquals("Group not found with id: 50", ex.getMessage());
    }

    @Test
    void shouldThrowWhenNoMatchesFoundForStage() {
        MatchRepository matchRepository = (MatchRepository) Proxy.newProxyInstance(
                MatchRepository.class.getClassLoader(),
                new Class<?>[]{MatchRepository.class},
                (proxy, method, args) -> {
                    if ("findByStageWithResult".equals(method.getName())) {
                        return List.of();
                    }
                    if ("toString".equals(method.getName())) {
                        return "MatchRepositoryProxy";
                    }
                    throw new UnsupportedOperationException("Method not supported in this test: " + method.getName());
                }
        );

        GroupRepository groupRepository = (GroupRepository) Proxy.newProxyInstance(
                GroupRepository.class.getClassLoader(),
                new Class<?>[]{GroupRepository.class},
                (proxy, method, args) -> true
        );

        StageRepository stageRepository = (StageRepository) Proxy.newProxyInstance(
                StageRepository.class.getClassLoader(),
                new Class<?>[]{StageRepository.class},
                (proxy, method, args) -> true
        );

        MatchService service = new MatchServiceImpl(matchRepository, groupRepository, stageRepository);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.getMatchesByGroupAndStage(null, 1L));

        assertEquals("No matches found for stage id: 1", ex.getMessage());
    }
}

