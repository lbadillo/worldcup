package com.lbd.app.tournament.service;

import java.util.List;

import com.lbd.app.tournament.dto.MatchDTO;
import com.lbd.app.tournament.dto.UserBetIDTO;

public interface MatchService {

    List<MatchDTO> getMatchesByGroupAndStage(Long groupId, Long stageId);

    List<UserBetIDTO> getMatchesByDate(String startDate, String endDate);
}

