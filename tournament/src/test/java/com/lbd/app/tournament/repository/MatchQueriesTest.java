package com.lbd.app.tournament.repository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

import com.lbd.app.tournament.repository.query.MatchQueries;

class MatchQueriesTest {

    @Test
    void shouldExposeFindByGroupAndStageQuery() {
        assertTrue(MatchQueries.FIND_BY_GROUP_AND_STAGE_WITH_RESULT.contains("where g.id = :groupId and s.id = :stageId"));
        assertTrue(MatchQueries.FIND_BY_GROUP_AND_STAGE_WITH_RESULT.contains("left join fetch m.result r"));
    }

    @Test
    void shouldExposeFindByStageQuery() {
        assertTrue(MatchQueries.FIND_BY_STAGE_WITH_RESULT.contains("where s.id = :stageId"));
        assertTrue(MatchQueries.FIND_BY_STAGE_WITH_RESULT.contains("left join fetch m.result r"));
    }

    @Test
    void shouldHavePrivateConstructor() throws Exception {
        Constructor<MatchQueries> constructor = MatchQueries.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);

        assertThrows(InvocationTargetException.class, constructor::newInstance);
    }
}

