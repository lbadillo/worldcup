package com.lbd.app.tournament.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.Query;

import com.lbd.app.tournament.repository.query.MatchQueries;

class MatchRepositoryTest {

    @Test
    void shouldUseExternalizedQuery() throws Exception {
        Method method = MatchRepository.class.getMethod("findByGroupAndStageWithResult", Long.class, Long.class);
        Query query = method.getAnnotation(Query.class);

        assertNotNull(query);
        assertEquals(MatchQueries.FIND_BY_GROUP_AND_STAGE_WITH_RESULT, query.value());
    }

    @Test
    void shouldUseExternalizedStageQuery() throws Exception {
        Method method = MatchRepository.class.getMethod("findByStageWithResult", Long.class);
        Query query = method.getAnnotation(Query.class);

        assertNotNull(query);
        assertEquals(MatchQueries.FIND_BY_STAGE_WITH_RESULT, query.value());
    }
}

