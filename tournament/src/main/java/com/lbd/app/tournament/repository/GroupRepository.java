package com.lbd.app.tournament.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lbd.app.tournament.model.Group;
import com.lbd.app.tournament.repository.query.GroupQueries;

public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query(GroupQueries.FIND_ALL_WITH_TEAMS)
    List<Group> findAllWithTeams();
}

