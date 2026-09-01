package com.lbd.app.tournament.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lbd.app.tournament.model.Stage;

public interface StageRepository extends JpaRepository<Stage, Long> {
}

