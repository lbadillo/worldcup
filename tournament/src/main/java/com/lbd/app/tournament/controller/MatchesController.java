package com.lbd.app.tournament.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lbd.app.tournament.dto.MatchDTO;
import com.lbd.app.tournament.service.MatchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchesController {

    private final MatchService matchService;

    @GetMapping("/groups/{groupId}/stages/{stageId}")
    public List<MatchDTO> getMatchesByGroupAndStage(
            @PathVariable Long groupId,
            @PathVariable Long stageId) {
        return matchService.getMatchesByGroupAndStage(groupId, stageId);
    }

    @GetMapping("/stages/{stageId}")
    public List<MatchDTO> getMatchesByStage(
            @PathVariable Long stageId) {
        return matchService.getMatchesByGroupAndStage(null,
                stageId);
    }
}

