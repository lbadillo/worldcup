package com.lbd.app.tournament.service;

import java.util.List;

import com.lbd.app.tournament.dto.MatchDTO;

public interface MatchService {

    List<MatchDTO> getMatchesByGroupAndStage(Long groupId, Long stageId);
}

