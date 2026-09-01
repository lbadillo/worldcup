package com.lbd.app.tournament.service;

import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;

import com.lbd.app.tournament.dto.AuthenticatedUserDTO;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;

public interface UserService extends OAuth2UserService<OAuth2UserRequest, OAuth2User> {



    OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest);

    List<AuthenticatedUserDTO> getAllUsers();

    AuthenticatedUserDTO getUserById(Long id);

    AuthenticatedUserDTO updateUser(AuthenticatedUserDTO data);

    AuthenticatedUserDTO getUserInfo();
}

