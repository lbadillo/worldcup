package com.lbd.app.tournament.integracion;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lbd.app.tournament.dto.UserBetDTO;
import com.lbd.app.tournament.model.Bet;
import com.lbd.app.tournament.model.Group;
import com.lbd.app.tournament.model.Match;
import com.lbd.app.tournament.model.Result;
import com.lbd.app.tournament.model.Stage;
import com.lbd.app.tournament.model.Team;
import com.lbd.app.tournament.model.User;
import com.lbd.app.tournament.model.UserRole;
import com.lbd.app.tournament.repository.BetRepository;
import com.lbd.app.tournament.repository.GroupRepository;
import com.lbd.app.tournament.repository.MatchRepository;
import com.lbd.app.tournament.repository.ResultRepository;
import com.lbd.app.tournament.repository.StageRepository;
import com.lbd.app.tournament.repository.TeamRepository;
import com.lbd.app.tournament.repository.UserRepository;
import com.lbd.app.tournament.repository.UserRoleRepository;
import com.lbd.app.tournament.util.GeneralConstants;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "app.security.enabled=true")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@ActiveProfiles("integration")
class MatchControllerIT {

    static final String TEST_EMAIL = "usuario.prueba@example.com";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    @BeforeAll
    static void setUp(
            @Autowired UserRepository userRepository,
            @Autowired UserRoleRepository userRoleRepository,
            @Autowired MatchRepository matchRepository,
            @Autowired TeamRepository teamRepository,
            @Autowired GroupRepository groupRepository,
            @Autowired StageRepository stageRepository,
            @Autowired BetRepository betRepository,
            @Autowired ResultRepository resultRepository) {
        betRepository.deleteAll();
        matchRepository.deleteAll();
        groupRepository.deleteAll();
        teamRepository.deleteAll();
        stageRepository.deleteAll();
        userRepository.deleteAll();

        Set<Team> teamsSaved = new LinkedHashSet<>();

        UserRole role = userRoleRepository.findByName(GeneralConstants.USER_ROLE_NAME)
                .orElseGet(() -> userRoleRepository.save(
                        UserRole.builder()
                                .name(GeneralConstants.USER_ROLE_NAME)
                                .build()
                ));


        User savedUser = userRepository.save(
                User.builder()
                        .name("Carlos Pérez")
                        .email(TEST_EMAIL)
                        .providerId("google")
                        .providerUserId("123456789")
                        .role(role)
                        .build()
        );

        for (int i = 0; i < 4; i++) {
            Team team = Team.builder().name("Team " + i)
                    .flag("flag" + i + ".png")
                    .wins(i)
                    .draws(i)
                    .losses(i)
                    .points(i * 3)
                    .build();
            teamsSaved.add(teamRepository.save(team));
        }


        Group groupSaved = groupRepository.save(Group.builder()
                .teams(teamsSaved)
                .name("A")
                .build());


        Stage stageSaved = stageRepository.save(Stage.builder().name("Group Stage").build());

        List<Team> listTeams = new ArrayList<>(teamsSaved);
        Match match1 = matchRepository.save(
                Match.builder().group(groupSaved)
                        .team1(listTeams.get(0))
                        .team2(listTeams.get(1))
                        .stage(stageSaved)
                        .dateMatch(Instant.parse("2026-06-10T15:30:00Z")).build());

        Match match2 = matchRepository.save(
                Match.builder().group(groupSaved)
                        .team1(listTeams.get(2))
                        .team2(listTeams.get(3))
                        .stage(stageSaved)
                        .dateMatch(Instant.parse("2026-06-10T15:30:00Z")).build());


        matchRepository.save(
                Match.builder().group(groupSaved)
                        .team1(listTeams.get(1))
                        .team2(listTeams.get(3))
                        .stage(stageSaved)
                        .dateMatch(Instant.parse("2026-07-10T15:30:00Z")).build());


        var m1 = matchRepository.save(match2);
        var m2 = matchRepository.save(match1);

        resultRepository.save(Result.builder().match(m1).value1(2).value2(1).build());
        resultRepository.save(Result.builder().match(m2).value1(1).value2(1).build());

        betRepository.save(
                Bet.builder()
                        .match(matchRepository.save(match1))
                        .user(savedUser)
                        .value1(2)
                        .value2(1)
                        .points(1)
                        .build());


    }


    @Test
    void getMatches_WhenAuthenticated_ShouldReturnUserFromDatabase() throws Exception {

        var res = mockMvc.perform(get("/matches/date/2026-06-10")

                        .with(jwt().jwt(jwt -> jwt.claim("email", TEST_EMAIL)))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        var resBody = res.getResponse().getContentAsString();

        List<UserBetDTO> matches = objectMapper.readValue(
                resBody,
                new TypeReference<List<UserBetDTO>>() {
                }
        );

        assertEquals(1,
                matches.stream().filter(match -> match.team1Name().equals(
                        "Team 0")).count());


    }


    @Test
    void getMatches_WhenUnauthenticated_ShouldRedirectToOAuth() throws Exception {
        mockMvc.perform(get("/matches/date/2026-06-10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getMatches_WhenDateNotInDatabase_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/matches/date/2026-06-15")
                        .with(jwt().jwt(jwt -> jwt.claim("email", TEST_EMAIL)))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$", hasSize(0)));
    }


}