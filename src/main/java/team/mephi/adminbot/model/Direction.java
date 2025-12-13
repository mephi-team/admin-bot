package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "directions")
public class Direction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    /**
     * Внешний идентификатор направления (курса) в системе NeoStudy.
     *
     * Здесь хранится ID этого направления,
     * под которым оно зарегистрировано в NeoStudy.
     */
    @Column(name = "neostudy_external_id")
    private String neostudyExternalId;

    /**
     * Дата и время последней синхронизации направления с NeoStudy.
     *
     * Показывает, когда данные курса
     * в последний раз обновлялись из NeoStudy
     * или отправлялись туда.
     */
    @Column(name = "neostudy_synced_at")
    private java.time.LocalDateTime neostudySyncedAt;

}
