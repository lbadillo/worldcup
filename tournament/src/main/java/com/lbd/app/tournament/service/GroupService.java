package com.lbd.app.tournament.service;

import java.util.List;

import com.lbd.app.tournament.dto.GroupDTO;

public interface GroupService {

    List<GroupDTO> getAllGroupsWithTeams();
}

