package team.mephi.adminbot.service;

import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.oauth2.core.oidc.StandardClaimAccessor;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationContext authContext;

    public AuthServiceImpl(AuthenticationContext authContext) {
        this.authContext = authContext;
    }

    @Override
    public StandardClaimAccessor getUserInfo() {
        DefaultOidcUser user = authContext.getAuthenticatedUser(DefaultOidcUser.class).orElseThrow();
        return user.getUserInfo();
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
