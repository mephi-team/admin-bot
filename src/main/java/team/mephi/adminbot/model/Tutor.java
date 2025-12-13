package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Сущность тьютора / куратора.
 *
 * Тьютор представляет собой преподавателя или куратора,
 * который работает со студентами по определенным направлениям.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tutor")
public class Tutor {

    /**
     * Внутренний ID тьютора в базе данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Полное имя тьютора (ФИО).
     */
    @Column(name = "full_name")
    private String fullName;

    /**
     * Username тьютора в Telegram.
     */
    @Column(name = "tg_name")
    private String tgName;

    /**
     * Telegram ID тьютора.
     *
     * Может быть пустым, если тьютор не имеет аккаунта в Telegram.
     */
    @Column(name = "tg_id")
    private String tgId;

    /**
     * Номер телефона тьютора.
     */
    @Column(name = "phone")
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

    // ===== Связи с другими сущностями =====

    /**
     * История назначений студентов на этого тьютора.
     *
     * Один тьютор может иметь много записей о назначениях студентов.
     * Связь через таблицу student_tutor.
     */
    @OneToMany(mappedBy = "tutor", fetch = FetchType.LAZY)
    @Builder.Default
    private List<StudentTutor> studentAssignments = new ArrayList<>();

    /**
     * Направления, с которыми работает тьютор.
     *
     * Связь многие-ко-многим через таблицу tutor_directions.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tutor_directions",
            joinColumns = @JoinColumn(name = "tutor_id"),
            inverseJoinColumns = @JoinColumn(name = "direction_id")
    )
    @Builder.Default
    private Set<Direction> directions = new HashSet<>();

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

