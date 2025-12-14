package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

/**
 * Сущность метаданных файлов в системе.
 *
 * <p>Этот класс представляет метаданные файлов, хранящихся в файловой системе.
 * Сами файлы (бинарные данные) НЕ хранятся в базе данных - только метаинформация:
 * имя файла, MIME-тип, путь к файлу в хранилище, размер, автор загрузки и время загрузки.
 *
 * <p>Файлы могут использоваться различными компонентами системы:
 * - шаблонами писем (mail templates)
 * - диалогами (dialogs)
 * - скриптами (scripts)
 * и другими модулями.
 *
 * <p>Важно: это метаданные хранилища файлов, не сами файлы.
 * Удаление записи из базы данных не должно автоматически удалять файл из файловой системы.
 */
@Entity
@Table(
        name = "files",
        indexes = {
                @Index(name = "idx_files_uploaded_by", columnList = "uploaded_by"),
                @Index(name = "idx_files_mime_type", columnList = "mime_type")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StoredFile {

    /**
     * Уникальный идентификатор файла в базе данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * Имя файла.
     *
     * <p>Оригинальное имя файла, которое было указано при загрузке.
     */
    @Column(nullable = false)
    private String filename;

    /**
     * MIME-тип файла.
     *
     * <p>Например: "image/png", "application/pdf", "text/plain" и т.д.
     */
    @Column(name = "mime_type", nullable = false)
    private String mimeType;

    /**
     * Путь к файлу в хранилище.
     *
     * <p>Относительный или абсолютный путь к файлу в файловой системе,
     * где физически хранится файл.
     */
    @Column(name = "storage_path", nullable = false)
    private String storagePath;

    /**
     * Размер файла в байтах.
     *
     * <p>Должен быть неотрицательным числом (>= 0).
     *
     * <p>Примечание: рекомендуется добавить CHECK constraint на уровне БД:
     * {@code ALTER TABLE files ADD CONSTRAINT chk_files_size_non_negative CHECK (size >= 0);}
     */
    @Column(nullable = false)
    private Long size;

    /**
     * Пользователь, который загрузил файл.
     *
     * <p>Связь с таблицей users через внешний ключ uploaded_by.
     * Один пользователь может загрузить множество файлов.
     *
     * <p>Загрузка выполняется лениво (LAZY) для оптимизации производительности.
     * Каскадное удаление отключено - удаление пользователя не должно
     * автоматически удалять загруженные им файлы.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by")
    private User uploadedBy;

    /**
     * Дата и время загрузки файла.
     *
     * <p>Автоматически устанавливается при создании записи и не изменяется
     * в дальнейшем (immutable).
     */
    @Column(name = "uploaded_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant uploadedAt;
}
