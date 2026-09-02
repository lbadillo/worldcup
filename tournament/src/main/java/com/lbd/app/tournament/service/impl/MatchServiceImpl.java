package com.lbd.app.tournament.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lbd.app.tournament.dto.MatchDTO;
import com.lbd.app.tournament.dto.MatchResultDTO;
import com.lbd.app.tournament.dto.TeamSummaryDTO;
import com.lbd.app.tournament.exception.ResourceNotFoundException;
import com.lbd.app.tournament.model.Match;
import com.lbd.app.tournament.repository.GroupRepository;
import com.lbd.app.tournament.repository.MatchRepository;
import com.lbd.app.tournament.repository.StageRepository;
import com.lbd.app.tournament.service.MatchService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final GroupRepository groupRepository;
    private final StageRepository stageRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MatchDTO> getMatchesByGroupAndStage(Long groupId, Long stageId) {
        if (!stageRepository.existsById(stageId)) {
            throw new ResourceNotFoundException("Stage not found with id: " + stageId);
        }

        if (groupId != null && !groupRepository.existsById(groupId)) {
            throw new ResourceNotFoundException("Group not found with id: " + groupId);
        }

        List<Match> matches = groupId == null
                ? matchRepository.findByStageWithResult(stageId)
                : matchRepository.findByGroupAndStageWithResult(groupId, stageId);

        if (matches.isEmpty()) {
            if (groupId == null) {
                throw new ResourceNotFoundException("No matches found for stage id: " + stageId);
            }
            throw new ResourceNotFoundException(
                    "No matches found for group id " + groupId + " and stage id " + stageId);
        }

        return matches.stream()
                .map(this::toMatchDto)
                .collect(Collectors.toList());
    }

    private MatchDTO toMatchDto(Match match) {
        MatchResultDTO result = match.getResult() == null
                ? new MatchResultDTO(0, 0)
                : new MatchResultDTO(match.getResult().getValue1(), match.getResult().getValue2());

        return new MatchDTO(
                match.getId(),
                match.getGroup().getId(),
                match.getStage().getId(),
                match.getStage().getName(),
                new TeamSummaryDTO(match.getTeam1().getId(), match.getTeam1().getName(), match.getTeam1().getFlag()),
                new TeamSummaryDTO(match.getTeam2().getId(), match.getTeam2().getName(), match.getTeam2().getFlag()),
                match.getDateMatch(),
                result
        );
    }
}

