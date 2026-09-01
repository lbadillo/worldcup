package com.lbd.app.tournament.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import com.lbd.app.tournament.dto.AuthenticatedUserDTO;
import com.lbd.app.tournament.exception.ResourceNotFoundException;
import com.lbd.app.tournament.model.User;
import com.lbd.app.tournament.model.UserRole;
import com.lbd.app.tournament.repository.UserRepository;
import com.lbd.app.tournament.service.impl.UserServiceImpl;
import com.lbd.app.tournament.util.GeneralConstants;

class UserServiceImplTest {

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldReturnUserInfoFromJwtAuthentication() {
        RepositoryState state = new RepositoryState();
        state.byEmail.put("jwt@example.com", userEntity(7L, "jwt@example.com", "JWT User", 2L, "USER"));
        UserService service = new UserServiceImpl(repository(state));

        Jwt jwt = Jwt.withTokenValue("jwt-token")
                .header("alg", "none")
                .claim("email", "jwt@example.com")
                .build();
        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(jwt));

        AuthenticatedUserDTO response = service.getUserInfo();

        assertEquals(7L, response.getId());
        assertEquals("jwt@example.com", response.getEmail());
        assertEquals("JWT User", response.getName());
        assertEquals(2L, response.getRoleId());
        assertEquals("USER", response.getRoleName());
    }

    @Test
    void shouldReturnUserInfoFromOAuth2Authentication() {
        RepositoryState state = new RepositoryState();
        state.byEmail.put("oauth@example.com", userEntity(9L, "oauth@example.com", "OAuth User", 2L, "USER"));
        UserService service = new UserServiceImpl(repository(state));

        DefaultOAuth2User principal = new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                Map.of("email", "oauth@example.com"),
                "email");
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(principal, null));

        AuthenticatedUserDTO response = service.getUserInfo();

        assertEquals(9L, response.getId());
        assertEquals("oauth@example.com", response.getEmail());
        assertEquals("OAuth User", response.getName());
    }

    @Test
    void shouldThrowWhenAuthenticatedEmailIsNotInRepository() {
        UserService service = new UserServiceImpl(repository(new RepositoryState()));

        Jwt jwt = Jwt.withTokenValue("jwt-token")
                .header("alg", "none")
                .claim("email", "missing@example.com")
                .build();
        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(jwt));

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, service::getUserInfo);

        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void shouldReturnAllUsersMappedToDto() {
        RepositoryState state = new RepositoryState();
        state.allUsers.add(userEntity(1L, "u1@example.com", "User 1", 1L, "ADMIN"));
        state.allUsers.add(userEntity(2L, "u2@example.com", "User 2", 2L, "USER"));

        UserService service = new UserServiceImpl(repository(state));
        List<AuthenticatedUserDTO> users = service.getAllUsers();

        assertEquals(2, users.size());
        assertEquals("u1@example.com", users.get(0).getEmail());
        assertEquals("ADMIN", users.get(0).getRoleName());
        assertEquals("u2@example.com", users.get(1).getEmail());
        assertEquals("USER", users.get(1).getRoleName());
    }

    @Test
    void shouldReturnUserById() {
        RepositoryState state = new RepositoryState();
        state.byId.put(10L, userEntity(10L, "id@example.com", "By Id", 2L, "USER"));

        UserService service = new UserServiceImpl(repository(state));
        AuthenticatedUserDTO response = service.getUserById(10L);

        assertEquals(10L, response.getId());
        assertEquals("id@example.com", response.getEmail());
    }

    @Test
    void shouldThrowWhenUserByIdNotFound() {
        UserService service = new UserServiceImpl(repository(new RepositoryState()));

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> service.getUserById(999L));

        assertEquals("Record not found", ex.getMessage());
    }

    @Test
    void shouldUpdateUserAndMapResponse() {
        RepositoryState state = new RepositoryState();
        UserService service = new UserServiceImpl(repository(state));

        AuthenticatedUserDTO request = AuthenticatedUserDTO.builder()
                .id(12L)
                .name("Updated")
                .email("updated@example.com")
                .providerId("google")
                .providerUserId("sub-123")
                .roleId(2L)
                .roleName("USER")
                .build();

        AuthenticatedUserDTO response = service.updateUser(request);

        assertEquals(12L, response.getId());
        assertEquals("updated@example.com", response.getEmail());
        assertEquals("sub-123", response.getProviderUserId());
        assertEquals("sub-123", response.getProviderId());
    }

    @Test
    void shouldRegisterNewUserAsAdminWhenNoUsersExist() throws Exception {
        RepositoryState state = new RepositoryState();
        state.existUsersValue = 0;
        UserServiceImpl service = new UserServiceImpl(repository(state));

        AuthenticatedUserDTO input = AuthenticatedUserDTO.builder()
                .name("Admin User")
                .email("admin@example.com")
                .providerId("google")
                .providerUserId("admin-sub")
                .build();

        User saved = (User) invokePrivate(service, "registerNewUser", new Class<?>[]{AuthenticatedUserDTO.class}, input);

        assertNotNull(saved);
        assertEquals(GeneralConstants.ADMIN_ROLE, saved.getRole().getId());
    }

    @Test
    void shouldRegisterNewUserAsUserWhenUsersExist() throws Exception {
        RepositoryState state = new RepositoryState();
        state.existUsersValue = 5;
        UserServiceImpl service = new UserServiceImpl(repository(state));

        AuthenticatedUserDTO input = AuthenticatedUserDTO.builder()
                .name("Regular User")
                .email("user@example.com")
                .providerId("google")
                .providerUserId("user-sub")
                .build();

        User saved = (User) invokePrivate(service, "registerNewUser", new Class<?>[]{AuthenticatedUserDTO.class}, input);

        assertNotNull(saved);
        assertEquals(GeneralConstants.USER_ROLE, saved.getRole().getId());
    }

    @Test
    void shouldKeepExplicitRoleWhenRegisteringNewUser() throws Exception {
        RepositoryState state = new RepositoryState();
        state.existUsersValue = 0;
        UserServiceImpl service = new UserServiceImpl(repository(state));

        AuthenticatedUserDTO input = AuthenticatedUserDTO.builder()
                .name("Custom Role")
                .email("custom@example.com")
                .providerId("google")
                .providerUserId("custom-sub")
                .roleId(99L)
                .build();

        User saved = (User) invokePrivate(service, "registerNewUser", new Class<?>[]{AuthenticatedUserDTO.class}, input);

        assertNotNull(saved);
        assertEquals(99L, saved.getRole().getId());
    }

    @Test
    void shouldUpdateExistingUserWhenProcessingOAuth2User() throws Exception {
        RepositoryState state = new RepositoryState();
        User existing = userEntity(50L, "old@example.com", "Old Name", 2L, "USER");
        state.byEmail.put("OAuth Name", existing);
        UserServiceImpl service = new UserServiceImpl(repository(state));

        OAuth2UserRequest request = oauth2UserRequest();
        OAuth2User oauth2User = new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                Map.of("sub", "new-sub", "name", "OAuth Name", "email", "new@example.com"),
                "sub");

        OAuth2User returned = (OAuth2User) invokePrivate(service,
                "processOAuth2User",
                new Class<?>[]{OAuth2UserRequest.class, OAuth2User.class},
                request, oauth2User);

        assertSame(oauth2User, returned);
        assertEquals("OAuth Name", state.lastSaved.getName());
        assertEquals("new-sub", state.lastSaved.getProviderUserId());
        assertEquals("google", state.lastSaved.getProviderId());
    }

    @Test
    void shouldRegisterUserWhenProcessingOAuth2UserAndNoExistingUser() throws Exception {
        RepositoryState state = new RepositoryState();
        state.existUsersValue = 0;
        UserServiceImpl service = new UserServiceImpl(repository(state));

        OAuth2UserRequest request = oauth2UserRequest();
        OAuth2User oauth2User = new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                Map.of("sub", "sub-1", "name", "New OAuth", "email", "new.oauth@example.com"),
                "sub");

        OAuth2User returned = (OAuth2User) invokePrivate(service,
                "processOAuth2User",
                new Class<?>[]{OAuth2UserRequest.class, OAuth2User.class},
                request, oauth2User);

        assertSame(oauth2User, returned);
        assertNotNull(state.lastSaved);
        assertEquals("New OAuth", state.lastSaved.getName());
        assertEquals("new.oauth@example.com", state.lastSaved.getEmail());
        assertEquals(GeneralConstants.ADMIN_ROLE, state.lastSaved.getRole().getId());
    }

    @Test
    void shouldMapEntityToDto() {
        UserServiceImpl service = new UserServiceImpl(repository(new RepositoryState()));
        User user = userEntity(33L, "map@example.com", "Mapped", 1L, "ADMIN");

        AuthenticatedUserDTO dto = service.toDTO(user);

        assertEquals(33L, dto.getId());
        assertEquals("map@example.com", dto.getEmail());
        assertEquals("ADMIN", dto.getRoleName());
    }

    private static Object invokePrivate(Object target, String methodName, Class<?>[] parameterTypes, Object... args)
            throws Exception {
        Method method = target.getClass().getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);
        try {
            return method.invoke(target, args);
        } catch (InvocationTargetException ex) {
            Throwable cause = ex.getCause();
            if (cause instanceof Exception exception) {
                throw exception;
            }
            throw ex;
        }
    }

    private static OAuth2UserRequest oauth2UserRequest() {
        ClientRegistration registration = ClientRegistration.withRegistrationId("google")
                .clientId("client-id")
                .clientSecret("client-secret")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .scope("openid", "profile", "email")
                .authorizationUri("https://example.com/auth")
                .tokenUri("https://example.com/token")
                .userInfoUri("https://example.com/userinfo")
                .userNameAttributeName("sub")
                .clientName("Google")
                .build();

        OAuth2AccessToken accessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                "token-value",
                Instant.now(),
                Instant.now().plusSeconds(120));

        return new OAuth2UserRequest(registration, accessToken);
    }

    private static UserRepository repository(RepositoryState state) {
        return (UserRepository) Proxy.newProxyInstance(
                UserRepository.class.getClassLoader(),
                new Class<?>[]{UserRepository.class},
                (proxy, method, args) -> {
                    String name = method.getName();
                    if ("findByEmail".equals(name)) {
                        return Optional.ofNullable(state.byEmail.get((String) args[0]));
                    }
                    if ("findAll".equals(name)) {
                        return state.allUsers;
                    }
                    if ("findById".equals(name)) {
                        return Optional.ofNullable(state.byId.get((Long) args[0]));
                    }
                    if ("existUsers".equals(name)) {
                        return state.existUsersValue;
                    }
                    if ("save".equals(name)) {
                        User user = (User) args[0];
                        state.lastSaved = user;
                        if (user.getId() == null) {
                            user.setId(++state.idCounter);
                        }
                        if (user.getEmail() != null) {
                            state.byEmail.put(user.getEmail(), user);
                        }
                        state.byId.put(user.getId(), user);
                        if (!state.allUsers.contains(user)) {
                            state.allUsers.add(user);
                        }
                        return user;
                    }
                    if ("toString".equals(name)) {
                        return "UserRepositoryProxy";
                    }
                    throw new UnsupportedOperationException("Method not supported in this test: " + name);
                }
        );
    }

    private static User userEntity(Long id, String email, String name, Long roleId, String roleName) {
        return User.builder()
                .id(id)
                .email(email)
                .name(name)
                .providerId("google")
                .providerUserId("provider-sub")
                .role(UserRole.builder().id(roleId).name(roleName).build())
                .build();
    }

    private static final class RepositoryState {
        private final Map<String, User> byEmail = new HashMap<>();
        private final Map<Long, User> byId = new HashMap<>();
        private final List<User> allUsers = new ArrayList<>();
        private int existUsersValue = 0;
        private long idCounter = 100L;
        private User lastSaved;
    }
}

