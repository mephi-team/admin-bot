package team.mephi.adminbot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import team.mephi.adminbot.model.enums.UserStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Основная сущность пользователя системы.
 *
 * Описывает кандидата или студента и содержит:
 * - личные данные
 * - роль и направление
 * - текущий статус
 * - связи с другими сущностями (диалоги, сообщения, вопросы и т.д.)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_email", columnList = "email", unique = true),
        @Index(name = "idx_users_tg_id", columnList = "tg_id", unique = true),
        @Index(name = "idx_users_phone_number", columnList = "phone_number", unique = true),
        @Index(name = "idx_users_last_name_phone", columnList = "last_name,phone_number"),
        @Index(name = "idx_users_last_name_email", columnList = "last_name,email"),
        @Index(name = "idx_users_status", columnList = "status"),
        @Index(name = "idx_users_role_code", columnList = "role_code"),
        @Index(name = "idx_users_direction_id", columnList = "direction_id")
})
public class User {

    /**
     * Внутренний ID пользователя в нашей базе данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Telegram ID пользователя.
     *
     * Уникальный идентификатор аккаунта в Telegram.
     */
    @Column(name = "tg_id", unique = true)
    private String tgId;

    /**
     * Username пользователя в Telegram.
     */
    @Column(name = "tg_name")
    private String tgName;

    /**
     * Имя пользователя.
     *
     * Основное поле с именем, которое используется в системе.
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * Email пользователя.
     *
     * Должен быть уникальным.
     */
    @Column(name = "email", unique = true)
    @NotBlank(message = "Email обязателен")
    @Email
    private String email;

    /**
     * Номер телефона пользователя.
     *
     * Также должен быть уникальным.
     */
    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "city", unique = true)
    private String city;

    /**
     * Роль пользователя в системе.
     *
     * Связь с таблицей ролей (roles.code).
     * Каждый пользователь имеет ровно одну роль.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_code", referencedColumnName = "code", nullable = false)
    @NotNull(message = "Роль обязательна")
    private Role role;

    /**
     * Направление / курс, к которому относится пользователь.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "direction_id")
    private Direction direction;

    /**
     * Когорта пользователя.
     *
     * Например, поток или набор.
     */
    @Column(name = "cohort")
    private String cohort;

    /**
     * Текущий статус пользователя.
     *
     * Например: активен, отчислён, заблокирован и т.п.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status;

    /**
     * Флаг согласия на обработку персональных данных.
     *
     * true — пользователь дал согласие
     * false — согласие не получено
     */
    @Column(name = "pd_consent", nullable = false)
    @Builder.Default
    private Boolean pdConsent = false;

    /**
     * Дата и время, когда пользователь дал согласие
     * на обработку персональных данных.
     */
    @Column(name = "pd_consent_at")
    private Instant pdConsentAt;

    /**
     * Флаг отзыва доступа пользователя.
     *
     * true — доступ отозван
     * false — доступ активен
     */
    @Column(name = "is_revoked", nullable = false)
    @Builder.Default
    private Boolean isRevoked = false;

    /**
     * Дата и время создания записи пользователя.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    /**
     * Дата и время последнего обновления записи пользователя.
     */
    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private Instant updatedAt;

    @Column
    private Boolean deleted;
    // ===== Устаревшие поля (оставлены для совместимости) =====

    /**
     * @deprecated Используй tgId.
     *
     * Старый внешний идентификатор пользователя
     * (Telegram, WhatsApp и т.п.).
     */
    @Deprecated
    @Column(name = "external_id", unique = true)
    private String externalId;

    /**
     * @deprecated Используй fullName.
     */
    @Deprecated
    @Column(name = "name")
    private String name;

    @Column(name = "first_name")
    @NotBlank(message = "Имя обязательно")
    @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов")
//    @NotEmpty(message = "Имя не должно быть пустым")
    private String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "Фамилия обязательна")
    @Size(min = 2, max = 50, message = "Фамилия должна быть от 2 до 50 символов")
//    @NotEmpty(message = "Фамилия не должна быть пустой")
    private String lastName;

    /**
     * ID пользователя в системе NeoStudy.
     *
     * Хранит идентификатор, под которым пользователь
     * зарегистрирован в NeoStudy.
     */
    @Column(name = "neostudy_external_id")
    private String neostudyExternalId;

    /**
     * Дата и время последней синхронизации пользователя с NeoStudy.
     */
    @Column(name = "neostudy_synced_at")
    private Instant neostudySyncedAt;

    // ===== Связи с другими сущностями =====

    /**
     * Диалоги, в которых участвует пользователь.
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Dialog> dialogs = new ArrayList<>();

    /**
     * Сообщения, отправленные пользователем.
     */
    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Message> messages = new ArrayList<>();

    /**
     * Вопросы, которые задал пользователь.
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private List<UserQuestion> userQuestions = new ArrayList<>();

    /**
     * Вопросы, назначенные пользователю как эксперту.
     */
    @OneToMany(mappedBy = "assignedExpert", fetch = FetchType.LAZY)
    @Builder.Default
    private List<UserQuestion> assignedQuestions = new ArrayList<>();

    /**
     * Заявки пользователя на поступление.
     *
     * Все заявки / формы записи, поданные этим пользователем.
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Application> applications = new ArrayList<>();

    /**
     * Связи пользователя с курсами (записи на обучение).
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private List<EnrollmentLink> enrollmentLinks = new ArrayList<>();

    /**
     * Записи рассылок, в которых пользователь является получателем.
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private List<MailingRecipient> mailingRecipients = new ArrayList<>();

    /**
     * Логи согласий на обработку персональных данных.
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private List<PdConsentLog> pdConsentLogs = new ArrayList<>();

    /**
     * Аудит-логи действий администратора,
     * где этот пользователь является исполнителем.
     */
    @OneToMany(mappedBy = "actor", fetch = FetchType.LAZY)
    @Builder.Default
    private List<AdminAudit> adminAudits = new ArrayList<>();

    /**
     * Логи регистрации пользователя.
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private List<RegistrationLog> registrationLogs = new ArrayList<>();

    /**
     * Файлы, загруженные пользователем.
     */
    @OneToMany(mappedBy = "uploadedBy", fetch = FetchType.LAZY)
    @Builder.Default
    private List<StoredFile> uploadedFiles = new ArrayList<>();

    /**
     * Ответы, которые дал пользователь.
     */
    @OneToMany(mappedBy = "answeredBy", fetch = FetchType.LAZY)
    @Builder.Default
    private List<UserAnswer> answers = new ArrayList<>();

    /**
     * История назначений этого пользователя (студента) на тьюторов.
     *
     * Один студент может иметь множество записей о назначениях на разных тьюторов.
     * Связь через таблицу student_tutor.
     */
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<StudentTutor> tutorAssignments = new HashSet<>();

    /**
     * Инициализация дат при создании записи.
     */
    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        if (this.createdAt == null) {
            this.createdAt = now;
        }
        if (this.updatedAt == null) {
            this.updatedAt = now;
        }
        this.deleted = false;
    }

    /**
     * Обновление даты изменения записи.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    /**
     * Пользователи считаются равными,
     * если у них совпадает ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}