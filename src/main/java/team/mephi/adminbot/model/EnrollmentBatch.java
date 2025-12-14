package team.mephi.adminbot.model;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import team.mephi.adminbot.model.enums.MailingStatus;

import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность батча (пакета) для массовой отправки ссылок на зачисление.
 *
 * Батч объединяет группу пользователей и описывает:
 * - кто его создал
 * - через какие каналы можно отправлять ссылки
 * - на каком этапе обработки он сейчас находится
 *
 * Используется для рассылок вне основного сценария регистрации.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "enrollment_batches")
public class EnrollmentBatch {

    /**
     * Уникальный идентификатор батча.
     *
     * Генерируется внешней логикой (не auto-increment).
     */
    @Id
    @Column(name = "batch_id", nullable = false)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * Пользователь, который создал этот батч.
     *
     * Обязательное поле — батч всегда имеет автора.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    /**
     * Дата и время создания батча.
     *
     * Устанавливается один раз при создании
     * и дальше не изменяется.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    /**
     * Текущий статус батча.
     *
     * Отражает этап жизненного цикла,
     * например: DRAFT, ACTIVE, PAUSED, FINISHED.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MailingStatus status;

    /**
     * Каналы доставки ссылок для этого батча.
     *
     * Хранится в виде JSON (jsonb в PostgreSQL).
     * Пример значения: ["telegram", "email"].
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "channels", columnDefinition = "jsonb", nullable = false)
    private JsonNode channels;

    /**
     * Ссылки на зачисление, входящие в этот батч.
     *
     * Один батч может содержать много ссылок.
     * Управление ссылками происходит отдельно
     * (без cascade-операций).
     */
    @OneToMany(
            mappedBy = "batch",
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<EnrollmentLink> enrollmentLinks = new ArrayList<>();

    /**
     * Инициализация значений перед сохранением батча в базу.
     *
     * Если значения не заданы явно:
     * - createdAt ставится в текущее время
     * - status устанавливается в DRAFT
     */
    @PrePersist
    protected void onCreate() {
        if (this.status == null) {
            this.status = MailingStatus.DRAFT;
        }
    }
}
