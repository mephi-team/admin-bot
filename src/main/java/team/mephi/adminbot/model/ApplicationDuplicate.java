package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.mephi.adminbot.model.enums.DuplicateFieldType;

/**
 * Сущность для хранения результатов обнаружения дубликатов заявок.
 *
 * Хранит информацию о том, что заявка является дубликатом другой заявки
 * по определенному полю (email, телефон, полное имя).
 *
 * Используется для:
 * - антидубликации заявок
 * - аналитики дубликатов
 * - связывания дубликатов с основной (primary) заявкой
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "application_duplicates", indexes = {
        @Index(name = "idx_application_duplicates_application_id", columnList = "application_id"),
        @Index(name = "idx_application_duplicates_primary_application_id", columnList = "primary_application_id"),
        @Index(name = "idx_application_duplicates_duplicate_field", columnList = "duplicate_field")
})
public class ApplicationDuplicate {

    /**
     * Внутренний ID записи о дубликате.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Заявка, которая является дубликатом.
     *
     * Связь с таблицей applications.
     * Обязательное поле.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    /**
     * Основная (primary) заявка, с которой обнаружен дубликат.
     *
     * Связь с таблицей applications.
     * Может быть null, если основная заявка еще не определена.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "primary_application_id")
    private Application primaryApplication;

    /**
     * Тип поля, по которому обнаружен дубликат.
     *
     * Может быть EMAIL, PHONE или FULL_NAME.
     * Обязательное поле.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "duplicate_field", nullable = false)
    private DuplicateFieldType duplicateField;

    /**
     * Значение поля, по которому обнаружен дубликат.
     *
     * Содержит фактическое значение (например, email адрес или номер телефона).
     * Обязательное поле.
     */
    @Column(name = "duplicate_value", nullable = false)
    private String duplicateValue;

    /**
     * Записи о дубликатах считаются равными,
     * если у них совпадает ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationDuplicate that = (ApplicationDuplicate) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

