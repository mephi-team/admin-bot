package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

/**
 * Сущность файла, загруженного в систему.
 *
 * <p>Представляет метаданные загруженных файлов, включая имя, тип, путь хранения,
 * размер, информацию о загрузившем пользователе и временную метку загрузки.
 *
 * <p>Особенности:
 * <ul>
 *   <li>Использует автоинкрементный первичный ключ (id)</li>
 *   <li>Хранит имя файла, MIME-тип, путь хранения и размер</li>
 *   <li>Связан с пользователем, который загрузил файл (uploadedBy)</li>
 *   <li>Автоматически сохраняет временную метку загрузки (uploadedAt)</li>
 * </ul>
 *
 * <p>Связи:
 * <ul>
 *   <li>uploadedBy: пользователь, загрузивший файл (FK → users.id)</li>
 * </ul>
 *
 * <p>Бизнес-логика:
 * <ul>
 *   <li>Один пользователь может загружать несколько файлов</li>
 *   <li>Удаление пользователя не удаляет файлы (логика приложения должна обрабатывать это)</li>
 * </ul>
 *
 * <p>Примечания для будущего расширения:
 * <ul>
 *   <li>Можно добавить поля для версионирования файлов или статуса обработки</li>
 *   <li>Текущая структура позволяет легко расширять функциональность</li>
 * </ul>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filename;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "storage_path", nullable = false)
    private String storagePath;

    @Column(nullable = false)
    private Long size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by")
    private User uploadedBy;

    @Column(name = "uploaded_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant uploadedAt;
}

