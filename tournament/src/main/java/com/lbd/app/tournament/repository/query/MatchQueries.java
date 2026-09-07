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

    public static final String FIND_BY_DATE_AND_USER = """
            SELECT 		mt.id as matchId,
             			t1.name as team1Name,
             			re.value_1 as team1Result,
             			bt.value_1 as team1Bet,
             			t2.name as team2Name,
             			re.value_2 as team2Result,
             			bt.value_2 as team2Bet,
             			gt.id as groupId,
             			gt.name as groupName,
             			st.name as stageName,
             			st.id as stageId,
             			bt.user_id as userId,
             			bt.points as points
             FROM  		match_team mt
             JOIN		team t1 on (mt.team_1_id = t1.id)
             JOIN    	team t2 on (mt.team_2_id = t2.id)
             JOIN		stage st on (mt.stage_id = st.id)
             LEFT JOIN	group_data gt on(mt.group_id = gt.id)
             LEFT JOIN   result re on (re.match_id = mt.id)
             LEFT JOIN	(
             				SELECT 	id,
             						user_id,
             						match_id,
             						value_1,
             						value_2,
             						points
             				FROM	bet
             				WHERE   user_id = :userId
             			) bt on (bt.match_id = mt.id )
             WHERE     mt.date_match BETWEEN  :startDate and :endDate
           """;
}

