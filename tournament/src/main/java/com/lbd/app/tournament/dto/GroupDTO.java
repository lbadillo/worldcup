package com.lbd.app.tournament.dto;

import java.util.List;

public record GroupDTO(Long id, String name, List<TeamDTO> teams) {
}

