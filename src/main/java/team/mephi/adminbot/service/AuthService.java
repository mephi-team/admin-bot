package team.mephi.adminbot.service;

import org.springframework.security.oauth2.core.oidc.StandardClaimAccessor;

public interface AuthService {
    StandardClaimAccessor getUserInfo();
    boolean isAdmin();
    void logout();
}
