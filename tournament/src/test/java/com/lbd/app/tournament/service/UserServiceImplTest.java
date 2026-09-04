package com.lbd.app.tournament.service;

import com.lbd.app.tournament.dto.AuthenticatedUserDTO;
import com.lbd.app.tournament.exception.ResourceNotFoundException;
import com.lbd.app.tournament.model.User;
import com.lbd.app.tournament.model.UserRole;
import com.lbd.app.tournament.repository.UserRepository;
import com.lbd.app.tournament.service.impl.UserServiceImpl;
import com.lbd.app.tournament.util.GeneralConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2Delegate;

    @InjectMocks
    private UserServiceImpl userService;

    private User sampleUser;
    private AuthenticatedUserDTO sampleDto;
    private OAuth2UserRequest oAuth2UserRequest;
    private OAuth2User mockOAuth2User;
    private User existingUser;

    @BeforeEach
    void setUp() {
        UserRole sampleRole = UserRole.builder()
                .id(1L)
                .name("ROLE_USER")
                .build();

        sampleUser = User.builder()
                .id(10L)
                .name("John Doe")
                .email("john.doe@example.com")
                .providerId("google")
                .providerUserId("google-sub-123")
                .role(sampleRole)
                .build();

        sampleDto = AuthenticatedUserDTO.builder()
                .id(10L)
                .name("John Doe")
                .email("john.doe@example.com")
                .providerId("google")
                .providerUserId("google-sub-123")
                .roleId(1L)
                .roleName("ROLE_USER")
                .build();
    }

    void setupAuth() {
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("google")
                .clientId("client-id")
                .clientSecret("client-secret")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost:8080/login/oauth2/code/google")
                .authorizationUri("https://accounts.google.com/o/oauth2/auth")
                .tokenUri("https://oauth2.googleapis.com/token")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .userNameAttributeName("sub")
                .clientName("Google")
                .build();

        OAuth2AccessToken accessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                "mock-token",
                null,
                null
        );

        oAuth2UserRequest = new OAuth2UserRequest(clientRegistration, accessToken);

        Map<String, Object> attributes = Map.of(
                "sub", "google-12345",
                "name", "John Doe",
                "email", "john.doe@example.com"
        );

        mockOAuth2User = new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "sub"
        );

        existingUser = User.builder()
                .id(1L)
                .name("Old Name")
                .email("john.doe@example.com")
                .providerId("google")
                .providerUserId("google-old-id")
                .role(UserRole.builder()
                        .id(GeneralConstants.USER_ROLE_ID)
                        .name(GeneralConstants.USER_ROLE_NAME)
                        .build())
                .build();
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldRegisterNewUserAsAdminWhenNoUsersExist() {
        setupAuth();
        when(oauth2Delegate.loadUser(oAuth2UserRequest)).thenReturn(mockOAuth2User);
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.empty());
        when(userRepository.existUsers()).thenReturn(0);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OAuth2User result = userService.loadUser(oAuth2UserRequest);

        assertNotNull(result);
        assertEquals("google-12345", result.getAttribute("sub"));
        verify(userRepository).save(any(User.class));
        verify(userRepository).existUsers();
    }

    @Test
    void shouldRegisterNewUserAsUserWhenUsersExist() {
        setupAuth();
        when(oauth2Delegate.loadUser(oAuth2UserRequest)).thenReturn(mockOAuth2User);
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.empty());
        when(userRepository.existUsers()).thenReturn(5);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OAuth2User result = userService.loadUser(oAuth2UserRequest);

        assertNotNull(result);
        verify(userRepository).save(any(User.class));
        verify(userRepository).existUsers();
    }

    @Test
    void shouldUpdateUserWhenUserAlreadyExists() {
        setupAuth();
        when(oauth2Delegate.loadUser(oAuth2UserRequest)).thenReturn(mockOAuth2User);
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OAuth2User result = userService.loadUser(oAuth2UserRequest);

        assertNotNull(result);
        assertEquals("John Doe", existingUser.getName());
        assertEquals("google-12345", existingUser.getProviderUserId());
        verify(userRepository).save(existingUser);
        verify(userRepository, never()).existUsers();
    }

    @Test
    void shouldReturnAllUsersMappedToDTO() {
        when(userRepository.findAll()).thenReturn(List.of(sampleUser));

        List<AuthenticatedUserDTO> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sampleUser.getEmail(), result.get(0).getEmail());
        verify(userRepository).findAll();
    }

    @Test
    void shouldReturnUserWhenIdExists() {
        when(userRepository.findById(10L)).thenReturn(Optional.of(sampleUser));

        AuthenticatedUserDTO result = userService.getUserById(10L);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("John Doe", result.getName());
        verify(userRepository).findById(10L);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUserById(99L)
        );

        assertEquals("Record not found", exception.getMessage());
        verify(userRepository).findById(99L);
    }

    @Test
    void shouldUpdateAndReturnUserDTO() {
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        AuthenticatedUserDTO result = userService.updateUser(sampleDto);

        assertNotNull(result);
        assertEquals(sampleDto.getEmail(), result.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldReturnUserInfoWhenAuthenticatedWithJwt() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaimAsString("email")).thenReturn("john.doe@example.com");

        JwtAuthenticationToken auth = new JwtAuthenticationToken(jwt);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(sampleUser));

        AuthenticatedUserDTO result = userService.getUserInfo();

        assertNotNull(result);
        assertEquals("john.doe@example.com", result.getEmail());
        verify(userRepository).findByEmail("john.doe@example.com");
    }

    @Test
    void shouldReturnUserInfoWhenAuthenticatedWithOAuth2User() {
        DefaultOAuth2User oAuth2User = mock(DefaultOAuth2User.class);
        when(oAuth2User.getAttribute("email")).thenReturn("john.doe@example.com");

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(oAuth2User);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(sampleUser));

        AuthenticatedUserDTO result = userService.getUserInfo();

        assertNotNull(result);
        assertEquals("john.doe@example.com", result.getEmail());
        verify(userRepository).findByEmail("john.doe@example.com");
    }

    @Test
    void shouldThrowResourceNotFoundWhenUserInContextNotInDatabase() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaimAsString("email")).thenReturn("notfound@example.com");

        JwtAuthenticationToken auth = new JwtAuthenticationToken(jwt);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserInfo());
        verify(userRepository).findByEmail("notfound@example.com");
    }
}