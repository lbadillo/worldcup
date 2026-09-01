package com.lbd.app.tournament.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lbd.app.tournament.model.Match;
import com.lbd.app.tournament.repository.query.MatchQueries;

public interface MatchRepository extends JpaRepository<Match, Long> {

    @Query(MatchQueries.FIND_BY_GROUP_AND_STAGE_WITH_RESULT)
    List<Match> findByGroupAndStageWithResult(@Param("groupId") Long groupId, @Param("stageId") Long stageId);

    @Query(MatchQueries.FIND_BY_STAGE_WITH_RESULT)
    List<Match> findByStageWithResult(@Param("stageId") Long stageId);
}

