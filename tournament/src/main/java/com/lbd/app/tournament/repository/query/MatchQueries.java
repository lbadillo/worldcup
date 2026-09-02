package com.lbd.app.tournament.repository.query;

public final class MatchQueries {

    private MatchQueries() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static final String FIND_BY_GROUP_AND_STAGE_WITH_RESULT = """
            select m from Match m
            join fetch m.group g
            join fetch m.stage s
            join fetch m.team1 t1
            join fetch m.team2 t2
            left join fetch m.result r
            where g.id = :groupId and s.id = :stageId
            order by m.dateMatch asc
            """;

    public static final String FIND_BY_STAGE_WITH_RESULT = """
            select m from Match m
            join fetch m.stage s
            join fetch m.team1 t1
            join fetch m.team2 t2
            left join fetch m.result r
            left join fetch m.group g
            where s.id = :stageId
            order by m.dateMatch asc
            """;
}

