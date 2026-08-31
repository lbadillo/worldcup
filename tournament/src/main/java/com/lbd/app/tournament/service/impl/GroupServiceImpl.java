package com.lbd.app.tournament.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lbd.app.tournament.dto.GroupDTO;
import com.lbd.app.tournament.dto.TeamDTO;
import com.lbd.app.tournament.model.Group;
import com.lbd.app.tournament.repository.GroupRepository;
import com.lbd.app.tournament.service.GroupService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    @Override
    @Transactional(readOnly = true)
    public List<GroupDTO> getAllGroupsWithTeams() {
        return groupRepository.findAllWithTeams().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private GroupDTO toDto(Group group) {
        return new GroupDTO(
                group.getId(),
                group.getName(),
                group.getTeams().stream()
                        .map(team -> new TeamDTO(
                                team.getId(),
                                team.getName(),
                                team.getFlag(),
                                team.getWins(),
                                team.getDraws(),
                                team.getLosses()))
                        .collect(Collectors.toList())
        );
    }
}

