package com.lbd.app.tournament.integracion;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

//@SpringBootTest(properties = "app.security.enabled=true")
//@AutoConfigureMockMvc
//@ActiveProfiles("integration")
class SecurityConfigIT {

    @Autowired
    private MockMvc mockMvc;

  //  @Test
    void shouldRequireAuthenticationWhenSecurityEnabled() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().isUnauthorized());
    }

    //@Test
    void shouldAllowAccessToMeWhenAuthenticatedWithOAuth2Login() throws Exception {
        mockMvc.perform(get("/user")
                        .with(oauth2Login().attributes(attributes -> {
                            attributes.put("sub", "google-sub-abc");
                            attributes.put("email", "sec@example.com");
                            attributes.put("name", "Sec User");
                        })))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sub").value("google-sub-abc"))
                .andExpect(jsonPath("$.email").value("sec@example.com"))
                .andExpect(jsonPath("$.name").value("Sec User"));
    }
}



