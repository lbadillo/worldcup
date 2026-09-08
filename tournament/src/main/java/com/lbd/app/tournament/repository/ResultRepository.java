package com.lbd.app.tournament.repository;

import com.lbd.app.tournament.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultRepository extends JpaRepository<Result, Long> {
}

