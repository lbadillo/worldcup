package com.lbd.app.tournament.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.Query;

import com.lbd.app.tournament.repository.query.GroupQueries;

class GroupRepositoryTest {

    @Test
    void shouldUseExternalizedQuery() throws Exception {
        Method method = GroupRepository.class.getMethod("findAllWithTeams");
        Query query = method.getAnnotation(Query.class);

        assertNotNull(query);
        assertEquals(GroupQueries.FIND_ALL_WITH_TEAMS, query.value());
    }
}

