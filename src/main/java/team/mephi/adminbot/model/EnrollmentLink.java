package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team.mephi.adminbot.model.enums.EnrollmentStatus;

import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

/**
 * Сущность ссылки для записи пользователя вне основного флоу.
 *
 * Проще говоря:
 * это персональная ссылка, по которой конкретный пользователь
 * может пройти и завершить процесс зачисления.
 *
 * Для каждой ссылки отслеживается:
 * - её жизненный цикл (создана → отправлена → использована / истекла / ошибка)
 * - была ли ссылка отправлена пользователю
 * - срок действия (если он есть)
 * - причина ошибки или истечения срока
 *
 * Связи:
 * - каждая ссылка относится к одному батчу зачисления
 * - каждая ссылка создаётся для одного конкретного пользователя
 *
 * Ограничения и правила:
 * - сама ссылка должна быть уникальной
 * - дата создания не меняется после сохранения
 * - дата истечения (если есть) должна быть позже даты создания
 * - флаг sent по умолчанию false
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "enrollment_links")
public class EnrollmentLink {

    /**
     * Технический первичный ключ записи.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * Батч зачисления, к которому относится эта ссылка.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id", nullable = false)
    private EnrollmentBatch batch;

    /**
     * Пользователь, для которого создана ссылка.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Сама ссылка (URL).
     *
     * Должна быть уникальной в рамках всей системы.
     */
    @Column(name = "link", nullable = false)
    private String link;

    /**
     * Дата и время создания ссылки.
     *
     * Проставляется автоматически
     * и больше никогда не изменяется.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    /**
     * Дата и время, после которых ссылка считается недействительной.
     *
     * Может быть null — тогда ссылка не имеет срока действия.
     * Если указана, должна быть позже createdAt.
     */
    @Column(name = "expires_at")
    private Instant expiresAt;

    /**
     * Признак того, что ссылка была отправлена пользователю.
     *
     * По умолчанию false.
     */
    @Column(name = "is_sent", nullable = false)
    @Builder.Default
    private boolean sent = false;

    /**
     * Текущий статус ссылки.
     *
     * Отражает её состояние:
     * PENDING, SENT, USED, EXPIRED, FAILED.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EnrollmentStatus status;

    /**
     * Причина текущего статуса.
     *
     * Используется, например, для ошибок
     * или пояснения, почему ссылка истекла.
     * Может быть пустым.
     */
    @Column(name = "status_reason")
    private String statusReason;

    /**
     * Метод, который вызывается перед сохранением новой ссылки.
     *
     * Делает следующее:
     * - гарантирует, что sent = false
     * - если статус не задан — ставит PENDING
     * - проверяет корректность даты истечения
     */
    @PrePersist
    protected void onCreate() {
        // Для новых ссылок всегда считаем, что они ещё не отправлены
        this.sent = false;

        if (this.status == null) {
            this.status = EnrollmentStatus.PENDING;
        }

        validateExpirationDate();
    }

    /**
     * Метод, который вызывается перед обновлением записи.
     *
     * Проверяет, что дата истечения
     * (если она указана) корректна.
     */
    @PreUpdate
    protected void onUpdate() {
        validateExpirationDate();
    }

    /**
     * Проверка корректности даты истечения.
     *
     * Если expiresAt указана, она должна быть
     * строго позже createdAt.
     *
     * Иначе выбрасывается исключение.
     */
    private void validateExpirationDate() {
        if (expiresAt != null && createdAt != null && !expiresAt.isAfter(createdAt)) {
            throw new IllegalArgumentException(
                    "expires_at must be after created_at. created_at: "
                            + createdAt + ", expires_at: " + expiresAt
            );
        }
    }
}
