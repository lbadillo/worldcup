package com.lbd.app.tournament.repository.query;

public final class UserQueries {
    /**
     * not allowed.
     */
    private UserQueries() {
        System.out.println("not allowed");
    }

    /**
     * query to test is table is empty.
     */
    public static final String EXISTS_USER =
            """
                            SELECT  count(*)
                            FROM    user_tournament
                            limit 1
                    """;


}
