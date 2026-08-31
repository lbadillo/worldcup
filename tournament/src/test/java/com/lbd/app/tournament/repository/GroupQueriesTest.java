package com.lbd.app.tournament.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

import com.lbd.app.tournament.repository.query.GroupQueries;

class GroupQueriesTest {

    @Test
    void shouldExposeQueryConstant() {
        assertEquals("select distinct g from Group g left join fetch g.teams", GroupQueries.FIND_ALL_WITH_TEAMS);
    }

    @Test
    void shouldHavePrivateConstructor() throws Exception {
        Constructor<GroupQueries> constructor = GroupQueries.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);

        assertThrows(InvocationTargetException.class, constructor::newInstance);
    }
}


