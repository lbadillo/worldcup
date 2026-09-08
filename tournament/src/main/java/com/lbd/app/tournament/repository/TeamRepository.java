package com.lbd.app.tournament.repository;

import com.lbd.app.tournament.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}

