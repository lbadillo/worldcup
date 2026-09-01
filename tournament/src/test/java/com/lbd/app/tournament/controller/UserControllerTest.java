package com.lbd.app.tournament.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.lbd.app.tournament.dto.AuthenticatedUserDTO;
import com.lbd.app.tournament.service.UserService;

class UserControllerTest {

    @Test
    void shouldReturnUserInfoFromService() {
        AuthenticatedUserDTO expected = AuthenticatedUserDTO.builder()
                .id(1L)
                .email("user@example.com")
                .name("John Doe")
                .build();

        UserController controller = new UserController(new StubUserService(expected));

        AuthenticatedUserDTO response = controller.getUser();

        assertSame(expected, response);
        assertEquals("user@example.com", response.getEmail());
    }

    private static final class StubUserService implements UserService {

        private final AuthenticatedUserDTO response;

        private StubUserService(AuthenticatedUserDTO response) {
            this.response = response;
        }

        @Override
        public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
            throw new UnsupportedOperationException("Not needed for this test");
        }

        @Override
        public List<AuthenticatedUserDTO> getAllUsers() {
            throw new UnsupportedOperationException("Not needed for this test");
        }

        @Override
        public AuthenticatedUserDTO getUserById(Long id) {
            throw new UnsupportedOperationException("Not needed for this test");
        }

        @Override
        public AuthenticatedUserDTO updateUser(AuthenticatedUserDTO data) {
            throw new UnsupportedOperationException("Not needed for this test");
        }

        @Override
        public AuthenticatedUserDTO getUserInfo() {
            return response;
        }
    }
}

