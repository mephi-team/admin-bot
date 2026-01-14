package team.mephi.adminbot.service;

import com.vaadin.flow.spring.security.AuthenticationContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private AuthenticationContext authContext;

    @Test
    void getUserInfoReturnsAuthenticatedUser() {
        DefaultOidcUser user = new DefaultOidcUser(
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                new OidcIdToken("token", Instant.now(), Instant.now().plusSeconds(60), Map.of("email", "user@example.com")),
                "email"
        );
        when(authContext.getAuthenticatedUser(DefaultOidcUser.class)).thenReturn(Optional.of(user));

        AuthServiceImpl service = new AuthServiceImpl(authContext);

        assertThat(service.getUserInfo()).isEqualTo(user);
    }

    @Test
    void isAdminChecksRoleList() {
        when(authContext.getGrantedRoles()).thenReturn(List.of("ADMIN", "USER"));
        AuthServiceImpl service = new AuthServiceImpl(authContext);

        assertThat(service.isAdmin()).isTrue();
    }

    @Test
    void logoutDelegatesToContext() {
        AuthServiceImpl service = new AuthServiceImpl(authContext);

        service.logout();

        verify(authContext).logout();
    }
}
