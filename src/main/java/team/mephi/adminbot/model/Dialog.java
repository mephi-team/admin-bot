package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.Data;
import team.mephi.adminbot.model.enums.DialogStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность диалога - агрегированная информация о диалоге с пользователем.
 *
 * <p>Это денормализованная сущность, которая хранит сводную информацию о диалоге:
 * <ul>
 *   <li>Последняя активность (lastMessageAt)</li>
 *   <li>Количество непрочитанных сообщений (unreadCount)</li>
 *   <li>Текущий статус диалога (status)</li>
 * </ul>
 *
 * <p>Жизненный цикл диалога:
 * <ol>
 *   <li>Создание: диалог создаётся при первом сообщении от пользователя (ACTIVE)</li>
 *   <li>Активность: обновляется lastMessageAt и unreadCount при каждом новом сообщении</li>
 *   <li>Закрытие: диалог может быть закрыт администратором (CLOSED)</li>
 *   <li>Архивация: старые диалоги могут быть архивированы (ARCHIVED)</li>
 *   <li>Блокировка: диалог может быть заблокирован при нарушении правил (BLOCKED)</li>
 * </ol>
 *
 * <p>Важно:
 * <ul>
 *   <li>unreadCount должен обновляться транзакционно вместе с сообщениями</li>
 *   <li>Не каскадирует удаление на User или Direction</li>
 *   <li>Все связи загружаются лениво (LAZY) для оптимизации производительности</li>
 * </ul>
 */
@Data
@Entity
@Table(name = "dialogs", indexes = {
        @Index(name = "idx_dialogs_user_id", columnList = "user_id"),
        @Index(name = "idx_dialogs_direction_id", columnList = "direction_id"),
        @Index(name = "idx_dialogs_status", columnList = "status"),
        @Index(name = "idx_dialogs_last_message_at", columnList = "last_message_at")
})
public class Dialog {

    /**
     * Внутренний ID диалога в базе данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Пользователь, которому принадлежит диалог.
     *
     * Один пользователь может иметь несколько диалогов (например, по разным направлениям).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Дата и время последнего сообщения в диалоге.
     *
     * Используется для сортировки диалогов и определения активности.
     * Обновляется автоматически при добавлении нового сообщения.
     */
    @Column(name = "last_message_at", nullable = false)
    private Instant lastMessageAt = Instant.now();

    /**
     * Количество непрочитанных сообщений в диалоге.
     *
     * Должно быть >= 0 (проверка на уровне базы данных через CHECK constraint).
     * Обновляется транзакционно вместе с сообщениями.
     *
     * Примечание: Для обеспечения целостности данных рекомендуется добавить
     * CHECK constraint в миграции базы данных:
     * ALTER TABLE dialogs ADD CONSTRAINT chk_dialogs_unread_count CHECK (unread_count >= 0);
     */
    @Column(nullable = false)
    private Integer unreadCount = 0;

    /**
     * Направление обучения, с которым связан диалог.
     *
     * Диалог может быть связан с конкретным направлением (курсом),
     * что помогает фильтровать и группировать диалоги.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "direction_id", nullable = false)
    private Direction direction;

    /**
     * Текущий статус диалога.
     *
     * Определяет состояние диалога: активен, закрыт, архивирован или заблокирован.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DialogStatus status = DialogStatus.ACTIVE;

    /**
     * Сообщения в этом диалоге.
     *
     * Связь один-ко-многим с таблицей dialog_messages.
     * Загружается лениво для оптимизации производительности.
     * Не каскадирует удаление - сообщения управляются отдельно.
     */
    @OneToMany(mappedBy = "dialog", fetch = FetchType.LAZY)
    private List<Message> messages = new ArrayList<>();

    // ===== equals() и hashCode() =====

    /**
     * Диалоги считаются одинаковыми, если у них совпадает ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dialog dialog = (Dialog) o;
        return id != null && id.equals(dialog.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
