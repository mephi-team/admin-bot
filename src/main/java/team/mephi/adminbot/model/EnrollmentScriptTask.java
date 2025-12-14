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
import team.mephi.adminbot.model.enums.ScriptTaskStatus;

import java.time.Instant;

/**
 * Сущность задачи выполнения скрипта массовой регистрации (enrollment script task).
 *
 * <p>Представляет выполнение скрипта регистрации для конкретного загруженного Excel-файла.
 * Каждая запись хранит статус выполнения, счётчики успешных и ошибочных операций,
 * временные метки начала и завершения, а также структурированные логи выполнения.
 *
 * <p>Жизненный цикл задачи:
 * <ul>
 *   <li><b>PENDING</b> — задача создана и ожидает выполнения</li>
 *   <li><b>RUNNING</b> — задача выполняется (started_at установлен)</li>
 *   <li><b>COMPLETED</b> — задача успешно завершена (finished_at установлен)</li>
 *   <li><b>FAILED</b> — при выполнении произошла ошибка (finished_at установлен)</li>
 * </ul>
 *
 * <p>Связи:
 * <ul>
 *   <li><b>file</b> — файл, для которого выполняется задача (ManyToOne, обязательная, LAZY)</li>
 * </ul>
 *
 * <p>Счётчики:
 * <ul>
 *   <li><b>successCount</b> — количество успешно обработанных строк (по умолчанию 0)</li>
 *   <li><b>errorCount</b> — количество строк с ошибками обработки (по умолчанию 0)</li>
 * </ul>
 *
 * <p>Временные метки:
 * <ul>
 *   <li><b>startedAt</b> — время начала выполнения (устанавливается при старте)</li>
 *   <li><b>finishedAt</b> — время завершения выполнения (устанавливается при завершении или ошибке)</li>
 * </ul>
 *
 * <p>Логирование:
 * <ul>
 *   <li><b>log</b> — структурированные данные выполнения и ошибок в формате JSON (jsonb)</li>
 * </ul>
 *
 * <p>Важно:
 * <ul>
 *   <li>Задача всегда принадлежит ровно одному EnrollmentScriptFile</li>
 *   <li>Удаление файла НЕ каскадирует удаление задач</li>
 *   <li>Счётчики инициализируются значением 0</li>
 *   <li>equals() и hashCode() основаны только на id</li>
 * </ul>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "enrollment_script_tasks")
public class EnrollmentScriptTask {
    /**
     * Уникальный идентификатор задачи.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * Файл, для которого выполняется задача.
     *
     * Всегда обязателен.
     * Загружается лениво.
     * Удаление файла НЕ удаляет задачи автоматически.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "file_id", nullable = false)
    private EnrollmentScriptFile file;

    /**
     * Статус выполнения задачи.
     *
     * Показывает текущее состояние выполнения:
     * PENDING, RUNNING, COMPLETED, FAILED.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ScriptTaskStatus status;

    /**
     * Количество успешно обработанных строк.
     *
     * По умолчанию 0.
     * Увеличивается при успешной обработке каждой строки Excel-файла.
     */
    @Column(name = "success_count", nullable = false)
    @Builder.Default
    private Integer successCount = 0;

    /**
     * Количество строк с ошибками обработки.
     *
     * По умолчанию 0.
     * Увеличивается при ошибке обработки каждой строки Excel-файла.
     */
    @Column(name = "error_count", nullable = false)
    @Builder.Default
    private Integer errorCount = 0;

    /**
     * Структурированные логи выполнения и ошибок.
     *
     * JSON-объект (jsonb) с деталями выполнения:
     * ошибки обработки строк, метаданные выполнения и т.д.
     * Может быть null, если логирование не требуется.
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "log", columnDefinition = "jsonb")
    private JsonNode log;

    /**
     * Время начала выполнения задачи.
     *
     * Устанавливается при старте выполнения скрипта.
     * Может быть null, если задача ещё не начала выполняться.
     */
    @Column(name = "started_at")
    private Instant startedAt;

    /**
     * Время завершения выполнения задачи.
     *
     * Устанавливается при завершении выполнения (успешном или с ошибкой).
     * Может быть null, если задача ещё не завершена.
     */
    @Column(name = "finished_at")
    private Instant finishedAt;
}

