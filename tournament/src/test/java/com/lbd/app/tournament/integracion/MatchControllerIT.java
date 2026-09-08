package com.lbd.app.tournament.integracion;

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
import com.lbd.app.tournament.repository.StageRepository;
import com.lbd.app.tournament.repository.TeamRepository;
import com.lbd.app.tournament.repository.UserRepository;
import com.lbd.app.tournament.repository.UserRoleRepository;
import com.lbd.app.tournament.util.GeneralConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "app.security.enabled=true")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration")
class MatchControllerIT {

    private final String TEST_EMAIL = "usuario.prueba@example.com";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private StageRepository stageRepository;
    @Autowired
    private BetRepository betRepository;

    @BeforeEach
    void setUp() {
        betRepository.deleteAll();
        matchRepository.deleteAll();
        groupRepository.deleteAll();
        teamRepository.deleteAll();
        stageRepository.deleteAll();
        userRepository.deleteAll();

        Set<Team> teamsSaved = new HashSet<>();

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

        for (int i = 0; i < 10; i++) {
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

        Instant tmp = Instant.parse("2026-06-10T15:30:00Z");

        Match match1 = matchRepository.save(
                Match.builder().group(groupSaved)
                        .team1(teamsSaved.iterator().next())
                        .team2(teamsSaved.iterator().next())
                        .stage(stageSaved)
                        .dateMatch(tmp).build());

        Match match2 = matchRepository.save(
                Match.builder().group(groupSaved)
                        .team1(teamsSaved.iterator().next())
                        .team2(teamsSaved.iterator().next())
                        .stage(stageSaved)
                        .dateMatch(tmp).build());

        Result result1 =
                Result.builder().match(match1).value1(2).value2(1).build();
        Result result2 =
                Result.builder().match(match2).value1(1).value2(1).build();


      //  match1.setResult(result1);
      //  match2.setResult(result2);
        matchRepository.save(match2);
        matchRepository.save(match1);
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
    void getUser_WhenAuthenticated_ShouldReturnUserFromDatabase() throws Exception {
        setUp();
        mockMvc.perform(get("/matches/date/2026-06-10")

                        .with(jwt().jwt(jwt -> jwt.claim("email", TEST_EMAIL)))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        /*
        .andExpect(jsonPath("$.id").value(savedUser.getId()))
                .andExpect(jsonPath("$.name").value("Carlos Pérez"))
                .andExpect(jsonPath("$.email").value(TEST_EMAIL))
                .andExpect(jsonPath("$.providerUserId").value("123456789"))
                .andExpect(jsonPath("$.roleName").value(GeneralConstants.USER_ROLE_NAME));

         */
    }

    /*
    @Test
    void getUser_WhenUnauthenticated_ShouldRedirectToOAuth() throws Exception {
        mockMvc.perform(get("/user")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getUser_WhenUserNotInDatabase_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/user")
                        .with(jwt().jwt(jwt -> jwt.claim("email", "no.existe@example.com")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    */

}