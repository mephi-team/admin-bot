package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;


/**
 * Сущность тьютора / куратора.
 * <p>
 * Тьютор представляет собой преподавателя или куратора,
 * который работает со студентами по определенным направлениям.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tutors")
public class Tutor {

    /**
     * Внутренний ID тьютора в базе данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    /**
     * Имя пользователя
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * Username тьютора в Telegram.
     */
    @Column(name = "tg_name")
    private String tgName;

    /**
     * Telegram ID тьютора.
     * <p>
     * Может быть пустым, если тьютор не имеет аккаунта в Telegram.
     */
    @Column(name = "tg_id")
    private String tgId;

    /**
     * Номер телефона тьютора.
     */
    @Column(name = "phone_number")
    private String phone;

    /**
     * Email адрес тьютора.
     */
    @Column(name = "email")
    private String email;

    /**
     * Дополнительные заметки о тьюторе.
     */
    @Column(name = "notes")
    private String notes;

    @Column(name = "deleted")
    private Boolean deleted;

    // ===== Связи с другими сущностями =====

    /**
     * История назначений студентов на этого тьютора.
     * <p>
     * Один тьютор может иметь много записей о назначениях студентов.
     * Связь через таблицу student_tutor.
     */
    @OneToMany(mappedBy = "tutor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<StudentTutor> studentAssignments = new HashSet<>();

    /**
     * Направления, с которыми работает тьютор.
     * <p>
     * Связь многие-ко-многим через таблицу tutor_directions.
     */
    @OneToMany(mappedBy = "tutor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<TutorDirection> directions = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        this.deleted = false;
    }
    // ===== equals() и hashCode() =====

    /**
     * Тьюторы считаются одинаковыми,
     * если у них совпадает ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tutor tutor = (Tutor) o;
        return id != null && id.equals(tutor.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

