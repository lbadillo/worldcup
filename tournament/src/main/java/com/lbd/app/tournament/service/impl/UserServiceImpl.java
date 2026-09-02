package com.lbd.app.tournament.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.lbd.app.tournament.exception.ResourceNotFoundException;
import com.lbd.app.tournament.model.User;
import com.lbd.app.tournament.model.UserRole;
import com.lbd.app.tournament.repository.UserRepository;
import com.lbd.app.tournament.util.GeneralConstants;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import com.lbd.app.tournament.dto.AuthenticatedUserDTO;
import com.lbd.app.tournament.service.UserService;

@Service
@NullMarked
public class UserServiceImpl implements UserService,
        OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2Delegate;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this(userRepository, new DefaultOAuth2UserService());
    }

    public UserServiceImpl(UserRepository userRepository,
                           OAuth2UserService<OAuth2UserRequest, OAuth2User>
                                   oauth2Delegate) {
        this.userRepository = userRepository;
        this.oauth2Delegate = oauth2Delegate;
    }

    @Override
    public OAuth2User loadUser(final OAuth2UserRequest oAuth2UserRequest) {
        OAuth2User oAuth2User = oauth2Delegate.loadUser(oAuth2UserRequest);
        return processOAuth2User(oAuth2UserRequest, oAuth2User);
    }

    @Override
    public List<AuthenticatedUserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream().map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AuthenticatedUserDTO getUserById(Long id) {
        return userRepository.findById(id).map(this::toDTO)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Record not found"));
    }

    @Override
    public AuthenticatedUserDTO updateUser(AuthenticatedUserDTO data) {
        return toDTO(userRepository.save(toEntity(data)));
    }

    @Override
    public AuthenticatedUserDTO getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            Jwt token = (Jwt) authentication.getPrincipal();
            return getUser(token.getClaimAsString("email"))
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        } else {
            assert authentication != null;
            DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
            assert user != null;
            return getUser(user.getAttribute("email"))
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        }
    }

    private Optional<AuthenticatedUserDTO> getUser(final String userName) {
        return userRepository.findByEmail(userName).map(entity ->
                AuthenticatedUserDTO.builder()
                        .id(entity.getId())
                        .providerUserId(entity.getProviderUserId())
                        .providerId(entity.getProviderId())
                        .name(entity.getName())
                        .email(entity.getEmail())
                        .roleId(entity.getRole().getId())
                        .roleName(entity.getRole().getName())
                        .build());
    }

    private OAuth2User processOAuth2User(
            final OAuth2UserRequest oAuth2UserRequest,
            final OAuth2User oAuth2User) {
        AuthenticatedUserDTO userInfoDto = AuthenticatedUserDTO.builder()
                .name(oAuth2User.getAttributes().get("name").toString())
                .providerUserId(oAuth2User.getAttributes().get("sub").toString())
                .email(oAuth2User.getAttributes().get("email").toString())
                .providerId(oAuth2UserRequest.getClientRegistration().getRegistrationId())
                .build();

        userRepository.findByEmail(userInfoDto.getEmail())
                .map(existingUser -> updateExistingUser(existingUser, userInfoDto))
                .orElseGet(() -> registerNewUser(userInfoDto));

        return oAuth2User;
    }

    private User registerNewUser(final AuthenticatedUserDTO userInfoDto) {
        if (Objects.isNull(userInfoDto.getRoleId())) {
            if (userRepository.existUsers() > 0) {
                userInfoDto.setRoleId(GeneralConstants.USER_ROLE);
            } else {
                userInfoDto.setRoleId(GeneralConstants.ADMIN_ROLE);
            }
        }
        return userRepository.save(
                User.builder()
                        .providerId(userInfoDto.getProviderId())
                        .name(userInfoDto.getName())
                        .email(userInfoDto.getEmail())
                        .providerUserId(userInfoDto.getProviderUserId())
                        .role(UserRole.builder().id(userInfoDto.getRoleId()).build())
                        .build());
    }

    private User updateExistingUser(final User existingUser, final AuthenticatedUserDTO userInfoDto) {
        existingUser.setName(userInfoDto.getName());
        existingUser.setProviderUserId(userInfoDto.getProviderUserId());
        existingUser.setProviderId(userInfoDto.getProviderId());
        return userRepository.save(existingUser);
    }

    private User toEntity(final AuthenticatedUserDTO data) {
        return User.builder()
                .id(data.getId())
                .name(data.getName())
                .email(data.getEmail())
                .providerId(data.getProviderUserId())
                .providerUserId(data.getProviderUserId())
                .role(UserRole.builder()
                        .id(data.getRoleId())
                        .name(data.getRoleName())
                        .build())
                .build();
    }

    public AuthenticatedUserDTO toDTO(final User entity) {
        return AuthenticatedUserDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .providerUserId(entity.getProviderUserId())
                .providerId(entity.getProviderId())
                .roleName(entity.getRole().getName())
                .roleId(entity.getRole().getId())
                .build();
    }
}