package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Сущность направления обучения (курса).
 * <p>
 * Примеры направлений: Аналитика, Java, Backend и т.п.
 * <p>
 * Это справочная сущность:
 * такие записи обычно не удаляются,
 * пока на них ссылаются пользователи, диалоги и другие данные.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "directions", indexes = {
        @Index(name = "idx_directions_code", columnList = "code", unique = true)
})
public class Direction {

    /**
     * Внутренний ID направления в базе данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Уникальный код направления.
     * <p>
     * Используется как технический идентификатор
     * (например: ANALYTICS, JAVA_BACKEND и т.п.).
     */
    @Column(nullable = false, unique = true)
    private String code;

    /**
     * Человекочитаемое название направления.
     * <p>
     * То, что показывается пользователям в интерфейсе.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Внешний идентификатор направления в системе NeoStudy.
     * <p>
     * Хранит ID курса, под которым он зарегистрирован в NeoStudy.
     */
    @Column(name = "neostudy_external_id")
    private String neostudyExternalId;

    /**
     * Дата и время последней синхронизации направления с NeoStudy.
     * <p>
     * Показывает, когда данные курса
     * последний раз обновлялись или отправлялись в NeoStudy.
     */
    @Column(name = "neostudy_synced_at")
    private java.time.Instant neostudySyncedAt;

    // ===== Связи с другими сущностями =====

    /**
     * Пользователи, относящиеся к этому направлению.
     * <p>
     * У одного направления может быть много пользователей.
     */
    @OneToMany(mappedBy = "direction", fetch = FetchType.LAZY)
    @Builder.Default
    private List<User> users = new ArrayList<>();

    /**
     * Заявки, связанные с этим направлением.
     * <p>
     * Все заявки на поступление, поданные на это направление обучения.
     */
    @OneToMany(mappedBy = "direction", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Application> applications = new ArrayList<>();

    /**
     * Диалоги, относящиеся к этому направлению.
     */
    @OneToMany(mappedBy = "direction", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Dialog> dialogs = new ArrayList<>();

    /**
     * Вопросы пользователей по этому направлению.
     */
    @OneToMany(mappedBy = "direction", fetch = FetchType.LAZY)
    @Builder.Default
    private List<UserQuestion> usersQuestions = new ArrayList<>();

    /**
     * Эксперты, работающие с этим направлением.
     * <p>
     * Связь многие-ко-многим через таблицу expert_directions.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "expert_directions",
            joinColumns = @JoinColumn(name = "direction_id"),
            inverseJoinColumns = @JoinColumn(name = "expert_id")
    )
    @Builder.Default
    private List<Expert> experts = new ArrayList<>();

    /**
     * Тьюторы, связанные с направлением.
     * <p>
     * Связь многие-ко-многим через таблицу tutor_directions.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tutor_directions",
            joinColumns = @JoinColumn(name = "direction_id"),
            inverseJoinColumns = @JoinColumn(name = "tutor_id")
    )
    @Builder.Default
    private List<Tutor> tutors = new ArrayList<>();

    // ===== equals() и hashCode() =====

    /**
     * Направления считаются одинаковыми,
     * если у них совпадает ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Direction direction = (Direction) o;
        return id != null && id.equals(direction.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
