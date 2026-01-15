package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Immutable;

import java.util.HashSet;
import java.util.Set;

/**
 * Справочник ролей пользователей системы.
 *
 * <p>Роли представляют собой статические справочные данные (reference data),
 * которые определяют права и возможности пользователей в системе.
 * Примеры ролей: visitor, student, lc_expert, extuser и т.д.
 *
 * <p>Каждая роль имеет уникальный код (code), который используется как первичный ключ
 * и для связи с пользователями через поле users.role_code.
 *
 * <p>Роли являются неизменяемыми справочными данными и не должны удаляться
 * каскадно при удалении пользователей.
 */
@Entity
@Table(name = "roles", uniqueConstraints = {
        @UniqueConstraint(name = "uk_roles_code", columnNames = "code")
})
@Immutable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Role {

    /**
     * Уникальный код роли.
     *
     * <p>Используется как первичный ключ и для связи с пользователями.
     * Поле является неизменяемым после создания (updatable = false на уровне БД).
     */
    @Id
    @Column(name = "code", nullable = false, unique = true, updatable = false)
    @EqualsAndHashCode.Include
    private String code;

    /**
     * Название роли.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Описание роли и её назначения в системе.
     */
    @Column(name = "description", nullable = false)
    private String description;

    /**
     * Пользователи, имеющие данную роль.
     *
     * <p>Связь один-ко-многим с сущностью User.
     * Загружается лениво (LAZY) по умолчанию.
     * Каскадное удаление отключено, так как роли являются справочными данными.
     */
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<User> users = new HashSet<>();
}
