package com.lbd.app.tournament.controller;

import java.util.List;

import com.lbd.app.tournament.dto.UserBetIDTO;
import com.lbd.app.tournament.util.annotations.ValidLocalDate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lbd.app.tournament.dto.MatchDTO;
import com.lbd.app.tournament.service.MatchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
@Validated
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

    @GetMapping("/date/{date}")
    public List<UserBetIDTO> getMatchesByDate(
            @PathVariable
            @ValidLocalDate
            String date) {
        return matchService.getMatchesByDate(date, date);
    }

}

