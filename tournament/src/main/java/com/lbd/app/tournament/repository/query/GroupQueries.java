package com.lbd.app.tournament.repository.query;

public final class GroupQueries {

    private GroupQueries() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static final String FIND_ALL_WITH_TEAMS = "select distinct g from Group g left join fetch g.teams";
}

