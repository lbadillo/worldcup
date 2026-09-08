package com.lbd.app.tournament.repository;

import com.lbd.app.tournament.model.Bet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BetRepository extends JpaRepository<Bet, Long> {
}

