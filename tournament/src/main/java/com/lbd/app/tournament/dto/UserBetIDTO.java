package com.lbd.app.tournament.dto;

public interface UserBetIDTO {
    Long getMatchId();

    String getTeam1Name();

    Integer getTeam1Result();

    Integer getTeam1Bet();

    String getTeam2Name();

    Integer getTeam2Result();

    Integer getTeam2Bet();

    Long getGroupId();

    String getGroupName();

    String getStageName();

    Long getStageId();

    Long getUserId();

    Integer getPoints();
}
