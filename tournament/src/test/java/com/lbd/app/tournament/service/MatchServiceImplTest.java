package com.lbd.app.tournament.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;



import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MatchServiceImplTest {

    private static Match match;
    private static Match matchWithoutResult;
    @InjectMocks
    private MatchServiceImpl service;
    @Mock
    private MatchRepository matchRepository;
    @Mock
    private StageRepository stageRepository;
    @Mock
    private GroupRepository groupRepository;

    @BeforeAll
    static void setUp() {
        Group group = Group.builder().id(10L).name("A").build();
        Stage stage = Stage.builder().id(1L).name("Group Stage").build();
        Team team1 = Team.builder().id(1L).name("Brazil").flag("br.png").points(10).wins(3).draws(1).losses(0).build();
        Team team2 = Team.builder().id(2L).name("Argentina").flag("ar.png").points(8).wins(2).draws(2).losses(0).build();

        match = Match.builder()
                .id(100L)
                .group(group)
                .stage(stage)
                .team1(team1)
                .team2(team2)
                .dateMatch(Instant.parse("2026-06-10T15:30:00Z"))
                .build();
        match.setResult(Result.builder().id(200L).match(match).value1(2).value2(1).build());

        matchWithoutResult = Match.builder()
                .id(101L)
                .group(group)
                .stage(stage)
                .team1(team2)
                .team2(team1)
                .dateMatch(Instant.parse("2026-06-11T15:30:00Z"))
                .build();


    }

    @Test
    void shouldMapMatchesByGroupAndStageToDTO() {

        when(groupRepository.existsById(10L)).thenReturn(true);
        when(stageRepository.existsById(1L)).thenReturn(true);

        when(matchRepository.findByGroupAndStageWithResult(10L, 1L))
                .thenReturn(List.of(match, matchWithoutResult));


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

        when(stageRepository.existsById(1L)).thenReturn(true);
        when(matchRepository.findByStageWithResult(1L))
                .thenReturn(List.of(match, matchWithoutResult));
        List<MatchDTO> response = service.getMatchesByGroupAndStage(null, 1L);

        assertEquals(2, response.size());
        assertEquals(10L, response.get(0).groupId());
        verify(matchRepository).findByStageWithResult(1L);

    }

    @Test
    void shouldThrowWhenStageDoesNotExist() {

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.getMatchesByGroupAndStage(10L, 99L));

        assertEquals("Stage not found with id: 99", ex.getMessage());
    }

    @Test
    void shouldThrowWhenGroupDoesNotExist() {
        when(stageRepository.existsById(1L)).thenReturn(true);
        when(groupRepository.existsById(50L)).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.getMatchesByGroupAndStage(50L, 1L));

        assertEquals("Group not found with id: 50", ex.getMessage());
    }

    @Test
    void shouldThrowWhenNoMatchesFoundForStage() {
        when(stageRepository.existsById(1L)).thenReturn(true);
        when(matchRepository.findByStageWithResult(1L)).thenReturn(List.of());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.getMatchesByGroupAndStage(null, 1L));

        assertEquals("No matches found for stage id: 1", ex.getMessage());
    }

    @Test
    void shouldThrowWhenNoMatchesFoundForGroupAndStage() {
        when(stageRepository.existsById(1L)).thenReturn(true);
        when(groupRepository.existsById(10L)).thenReturn(true);
        when(matchRepository.findByGroupAndStageWithResult(10L, 1L)).thenReturn(List.of());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.getMatchesByGroupAndStage(10L, 1L));

        assertEquals("No matches found for group id 10 and stage id 1", ex.getMessage());
    }
}

