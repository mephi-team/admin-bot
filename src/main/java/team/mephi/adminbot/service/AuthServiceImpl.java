package team.mephi.adminbot.service;

import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Реализация сервиса получения информации о пользователе.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationContext authContext;

    /**
     * Конструктор сервиса аутентификации.
     *
     * @param authContext контекст аутентификации.
     */
    public AuthServiceImpl(AuthenticationContext authContext) {
        this.authContext = authContext;
    }

    @Override
    @SuppressWarnings("unchecked")
    public DefaultOidcUser getUserInfo() {
        Object principal = authContext.getAuthenticatedUser(Object.class).orElseThrow();
        if (principal instanceof Jwt) {
            OidcIdToken idToken = new OidcIdToken(
                    ((Jwt) principal).getTokenValue(),
                    ((Jwt) principal).getIssuedAt(),
                    ((Jwt) principal).getExpiresAt(),
                    ((Jwt) principal).getClaims()
            );
            var roles = ((Map<String, List<String>>)((Jwt)principal).getClaim("realm_access")).get("roles").stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r.toUpperCase())).toList();
            return new DefaultOidcUser(
                    roles,
                    idToken,
                    "sub"
            );
        } else if (principal instanceof DefaultOidcUser) {
            return (DefaultOidcUser) principal;
        }
        return null;
    }

    @Override
    public boolean isAdmin() {
        Collection<String> userRoles = authContext.getGrantedRoles();
        return userRoles.contains("ADMIN");
    }

    @Override
    public void logout() {
        authContext.logout();
    }
}
