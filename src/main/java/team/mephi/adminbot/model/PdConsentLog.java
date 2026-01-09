package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import team.mephi.adminbot.model.enums.ConsentStatus;

import java.time.Instant;

/**
 * Лог согласий на обработку персональных данных (PD / GDPR).
 *
 * <p>Эта сущность хранит историю всех событий согласия пользователей на обработку персональных данных.
 * Каждая запись представляет одно неизменяемое событие согласия (предоставлено, отозвано, обновлено)
 * с метаданными о его источнике и статусе.
 *
 * <p>Записи в этой таблице являются append-only (только добавление, без обновлений и удалений после вставки)
 * для обеспечения соответствия требованиям аудита и защиты данных (GDPR, ФЗ-152 и т.п.).
 *
 * <p>Каждая запись содержит:
 * <ul>
 *   <li>Пользователя, для которого было зарегистрировано событие согласия</li>
 *   <li>Временную метку события (consented_at)</li>
 *   <li>Источник согласия (telegram_bot, web_form, admin_panel, import и т.д.)</li>
 *   <li>Статус согласия (GRANTED, REVOKED, PENDING)</li>
 * </ul>
 *
 * <p>Связь с пользователем: один пользователь может иметь множество записей в логе согласий.
 * Связь с аудитом: записи могут быть связаны с admin_audit для отслеживания изменений согласий администраторами.
 *
 * <p><strong>Важно:</strong> Эта сущность предназначена для юридического аудита и соответствия требованиям
 * защиты персональных данных. Записи никогда не должны обновляться или удаляться после создания.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "pd_consent_log")
public class PdConsentLog {

    /**
     * Уникальный идентификатор записи лога согласия.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * Пользователь, для которого было зарегистрировано событие согласия.
     * <p>
     * Связь: Many-to-One (много записей лога для одного пользователя).
     * Загрузка: LAZY (ленивая загрузка для оптимизации производительности).
     * Каскады: без каскадного удаления (REMOVE не применяется к User).
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Дата и время, когда произошло событие согласия.
     * <p>
     * Устанавливается автоматически при создании записи (если не задано явно)
     * и не может быть изменена после вставки. Это поле является неизменяемым (immutable)
     * для обеспечения целостности аудита.
     */
    @Column(name = "consented_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant consentedAt;

    /**
     * Источник согласия, откуда пришло согласие пользователя.
     * <p>
     * Примеры значений: telegram_bot, web_form, admin_panel, import, api и т.д.
     * Это поле помогает отслеживать, каким способом пользователь предоставил или отозвал согласие.
     */
    @Column(name = "source", nullable = false)
    private String source;

    /**
     * Статус согласия на момент события.
     * <p>
     * Может быть: GRANTED (предоставлено), REVOKED (отозвано), PENDING (ожидает обработки).
     * Это поле отражает состояние согласия в момент регистрации события.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ConsentStatus status;

}

