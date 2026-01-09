package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import team.mephi.adminbot.model.enums.ScriptTaskStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Загруженный Excel-файл для скрипта массовой регистрации.
 * <p>
 * Каждый файл — это отдельный источник данных,
 * который проходит определённый жизненный цикл обработки:
 * <p>
 * - PENDING   — файл загружен и ожидает обработки
 * - RUNNING   — файл сейчас обрабатывается
 * - COMPLETED — обработка успешно завершена
 * - FAILED    — при обработке произошла ошибка
 * <p>
 * Файл связан с задачами обработки (EnrollmentScriptTask):
 * один файл → несколько задач.
 * При удалении файла связанные задачи автоматически НЕ удаляются.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "enrollment_script_files")
public class EnrollmentScriptFile {

    /**
     * Уникальный идентификатор файла.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * Имя файла в том виде,
     * в котором его загрузил пользователь.
     */
    @Column(name = "filename", nullable = false)
    private String filename;

    /**
     * Пользователь, который загрузил файл.
     * <p>
     * Всегда обязателен.
     * Загружается лениво.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by", nullable = false)
    private User uploadedBy;

    /**
     * Дата и время загрузки файла.
     * <p>
     * Устанавливается один раз при создании
     * и дальше не изменяется.
     */
    @Column(name = "uploaded_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant uploadedAt;

    /**
     * Текущий статус обработки файла.
     * <p>
     * Показывает, на каком этапе
     * находится обработка.
     * <p>
     * По умолчанию — PENDING.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ScriptTaskStatus status;

    /**
     * Задачи обработки, связанные с этим файлом.
     * <p>
     * Один файл может иметь несколько задач.
     * Удаление файла НЕ удаляет задачи автоматически.
     */
    @OneToMany(
            mappedBy = "file",
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<EnrollmentScriptTask> tasks = new ArrayList<>();

    /**
     * Инициализация значений перед сохранением в базу.
     * <p>
     * Если значения не заданы:
     * - uploadedAt ставится в текущее время
     * - status устанавливается в PENDING
     */
    @PrePersist
    protected void onCreate() {
        if (this.status == null) {
            this.status = ScriptTaskStatus.PENDING;
        }
    }
}
