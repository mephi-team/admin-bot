package team.mephi.adminbot.config;

import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Кастомный конвертер JWT-токена в объект аутентификации Spring Security.
 * <p>
 * Берёт роли из JWT, который приходит от Keycloak.
 * <p>
 * Поддерживаются два варианта ролей:
 * 1. Роли на уровне realm (realm_access.roles)
 * 2. Роли конкретного клиента (resource_access.{client-id}.roles)
 * <p>
 * Все роли приводятся к формату Spring Security:
 * добавляется префикс "ROLE_" и роль переводится в верхний регистр.
 */
public class JwtAuthenticationConverter implements Converter<Jwt, JwtAuthenticationToken> {

    // Стандартный конвертер Spring Security (достаёт роли из scope / authorities)
    private final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter =
            new JwtGrantedAuthoritiesConverter();

    // ID клиента в Keycloak, для которого будем доставать роли
    private final String clientId;

    public JwtAuthenticationConverter(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public JwtAuthenticationToken convert(@NonNull Jwt jwt) {
        // Объединяем стандартные роли Spring Security
        // и роли, которые достаём из Keycloak вручную
        Collection<GrantedAuthority> authorities = Stream.concat(
                defaultGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractKeycloakRoles(jwt).stream()
        ).collect(Collectors.toSet());

        // Возвращаем токен аутентификации с полным списком ролей
        return new JwtAuthenticationToken(jwt, authorities);
    }

    /**
     * Достаёт все роли из JWT, который пришёл от Keycloak.
     */
    private Collection<GrantedAuthority> extractKeycloakRoles(Jwt jwt) {
        // Получаем роли уровня realm
        Collection<GrantedAuthority> realmRoles = extractRealmRoles(jwt);

        // Получаем роли конкретного клиента
        Collection<GrantedAuthority> clientRoles = extractClientRoles(jwt, clientId);

        // Объединяем оба набора ролей в один список
        return Stream.concat(realmRoles.stream(), clientRoles.stream())
                .collect(Collectors.toSet());
    }

    /**
     * Достаёт роли уровня realm из поля realm_access.roles.
     */
    @SuppressWarnings("unchecked")
    private Collection<GrantedAuthority> extractRealmRoles(Jwt jwt) {
        // Получаем объект realm_access из токена
        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
        if (realmAccess == null) {
            // Если ролей нет — возвращаем пустой список
            return Collections.emptyList();
        }

        // Берём список ролей
        List<String> roles = (List<String>) realmAccess.get("roles");
        if (roles == null) {
            return Collections.emptyList();
        }

        // Преобразуем каждую роль в формат ROLE_*
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .collect(Collectors.toList());
    }

    /**
     * Достаёт роли клиента из поля resource_access.{client-id}.roles.
     */
    @SuppressWarnings("unchecked")
    private Collection<GrantedAuthority> extractClientRoles(Jwt jwt, String clientId) {
        // Получаем объект resource_access из токена
        Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
        if (resourceAccess == null) {
            return Collections.emptyList();
        }

        // Получаем данные конкретного клиента по clientId
        Map<String, Object> clientAccess =
                (Map<String, Object>) resourceAccess.get(clientId);
        if (clientAccess == null) {
            return Collections.emptyList();
        }

        // Берём список ролей клиента
        List<String> roles = (List<String>) clientAccess.get("roles");
        if (roles == null) {
            return Collections.emptyList();
        }

        // Преобразуем роли в формат Spring Security
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .collect(Collectors.toList());
    }
}
