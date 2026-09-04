package com.lbd.app.tournament.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lbd.app.tournament.dto.GroupDTO;
import com.lbd.app.tournament.service.GroupService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupsController {

    private final GroupService groupService;


    @GetMapping
    public ResponseEntity<List<GroupDTO>> getAllGroups() {
        return ResponseEntity.ok(groupService.getAllGroupsWithTeams());
    }

    //TODO delete this method when the security is implemented
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_USER')")
    @GetMapping("/test")
    public ResponseEntity<String> getTest() {
        return ResponseEntity.ok("Test successful");
    }

}

