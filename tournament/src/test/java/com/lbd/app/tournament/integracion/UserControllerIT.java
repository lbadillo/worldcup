package com.lbd.app.tournament.integracion;

import com.lbd.app.tournament.model.User;
import com.lbd.app.tournament.model.UserRole;
import com.lbd.app.tournament.repository.UserRepository;
import com.lbd.app.tournament.repository.UserRoleRepository;
import com.lbd.app.tournament.util.GeneralConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "app.security.enabled=true")
@AutoConfigureMockMvc
@ActiveProfiles("integration")
class UserControllerIT {

    private final String TEST_EMAIL = "usuario.prueba@example.com";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    private User savedUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        UserRole role =
                userRoleRepository.findByName(GeneralConstants.USER_ROLE_NAME)
                        .orElseGet(()-> UserRole.builder().build());

        savedUser = userRepository.save(
                User.builder()
                        .name("Carlos Pérez")
                        .email(TEST_EMAIL)
                        .providerId("google")
                        .providerUserId("123456789")
                        .role(role)
                        .build()
        );
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void getUser_WhenAuthenticated_ShouldReturnUserFromDatabase() throws Exception {
        mockMvc.perform(get("/user")

                        .with(jwt().jwt(jwt -> jwt.claim("email", TEST_EMAIL)))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedUser.getId()))
                .andExpect(jsonPath("$.name").value("Carlos Pérez"))
                .andExpect(jsonPath("$.email").value(TEST_EMAIL))
                .andExpect(jsonPath("$.providerUserId").value("123456789"))
                .andExpect(jsonPath("$.roleName").value(GeneralConstants.USER_ROLE_NAME));
    }

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
}