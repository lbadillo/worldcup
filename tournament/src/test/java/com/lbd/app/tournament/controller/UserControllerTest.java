package com.lbd.app.tournament.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.lbd.app.tournament.dto.AuthenticatedUserDTO;
import com.lbd.app.tournament.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Test
    void shouldReturnUserInfoFromService() {
        AuthenticatedUserDTO expected = AuthenticatedUserDTO.builder()
                .id(1L)
                .email("user@example.com")
                .name("John Doe")
                .build();

        when(userService.getUserInfo()).thenReturn(expected);

        AuthenticatedUserDTO response = userController.getUser();

        assertSame(expected, response);
        assertEquals("user@example.com", response.getEmail());
    }


}

