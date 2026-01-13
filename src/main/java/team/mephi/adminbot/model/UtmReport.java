package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Сущность для хранения агрегированной статистики по UTM-меткам
 * за определённый период времени.
 * <p>
 * Проще говоря:
 * здесь лежат уже посчитанные цифры для аналитики и отчётов.
 * Одна строка = одна комбинация UTM-параметров + период дат.
 * <p>
 * Основные особенности:
 * - Таблица почти всегда используется только для чтения
 * - Данные обновляются пакетно (джобами), а не руками пользователей
 * - Нет связей с другими таблицами (это чистая аналитика)
 * - Некоторые поля (ctr и conversion) — это уже рассчитанные показатели
 * <p>
 * Формулы метрик:
 * - CTR (кликабельность) = клики / показы (если показы учитываются)
 * - Conversion (конверсия) = регистрации / уникальные пользователи
 * <p>
 * Бизнес-ключ:
 * Запись однозначно определяется комбинацией:
 * (periodStart, periodEnd, utmSource, utmMedium,
 * utmCampaign, utmTerm, utmContent)
 * <p>
 * Это жёстко зафиксировано уникальным ограничением в базе.
 * <p>
 * Важно:
 * В обычной бизнес-логике эту сущность нужно считать read-only.
 * Обновления выполняются только агрегирующими фоновыми задачами.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Immutable
@Table(
        name = "utm_reports",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_utm_reports_composite",
                columnNames = {
                        "period_start",
                        "period_end",
                        "utm_source",
                        "utm_medium",
                        "utm_campaign",
                        "utm_term",
                        "utm_content"
                }
        )
)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UtmReport {

    /**
     * Технический первичный ключ записи в базе.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Дата начала отчётного периода.
     * Входит в состав бизнес-ключа.
     */
    @EqualsAndHashCode.Include
    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    /**
     * Дата окончания отчётного периода.
     * Входит в состав бизнес-ключа.
     */
    @EqualsAndHashCode.Include
    @Column(name = "period_end", nullable = false)
    private LocalDate periodEnd;

    /**
     * Источник трафика (utm_source),
     * например: google, facebook и т.д.
     * Входит в состав бизнес-ключа.
     */
    @EqualsAndHashCode.Include
    @Column(name = "utm_source", nullable = false)
    private String utmSource;

    /**
     * Тип трафика (utm_medium),
     * например: cpc, email, social.
     * Входит в состав бизнес-ключа.
     */
    @EqualsAndHashCode.Include
    @Column(name = "utm_medium", nullable = false)
    private String utmMedium;

    /**
     * Название рекламной кампании (utm_campaign).
     * Входит в состав бизнес-ключа.
     */
    @EqualsAndHashCode.Include
    @Column(name = "utm_campaign", nullable = false)
    private String utmCampaign;

    /**
     * Дополнительный параметр utm_term.
     * Обычно используется для ключевых слов.
     * Может быть пустым.
     * Входит в состав бизнес-ключа.
     */
    @EqualsAndHashCode.Include
    @Column(name = "utm_term")
    private String utmTerm;

    /**
     * Дополнительный параметр utm_content.
     * Например: тип баннера или ссылка.
     * Может быть пустым.
     * Входит в состав бизнес-ключа.
     */
    @EqualsAndHashCode.Include
    @Column(name = "utm_content")
    private String utmContent;

    /**
     * Общее количество кликов по этой UTM-комбинации
     * за выбранный период.
     */
    @Builder.Default
    @Column(nullable = false)
    private Integer clicks = 0;

    /**
     * Количество уникальных пользователей,
     * которые кликнули по ссылке.
     */
    @Builder.Default
    @Column(name = "unique_users", nullable = false)
    private Integer uniqueUsers = 0;

    /**
     * Количество регистраций,
     * пришедших с этой UTM-комбинации.
     */
    @Builder.Default
    @Column(nullable = false)
    private Integer registrations = 0;

    /**
     * CTR (кликабельность).
     * Отношение кликов к показам.
     * <p>
     * Хранится в BigDecimal с точностью до 4 знаков
     * для корректных аналитических расчётов.
     */
    @Builder.Default
    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal ctr = BigDecimal.ZERO;

    /**
     * Конверсия.
     * Отношение регистраций к количеству уникальных пользователей.
     * <p>
     * Также хранится с высокой точностью
     * для использования в отчётах и дашбордах.
     */
    @Builder.Default
    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal conversion = BigDecimal.ZERO;
}
