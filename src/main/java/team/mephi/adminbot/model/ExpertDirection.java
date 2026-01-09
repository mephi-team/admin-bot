package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Сущность связи эксперта с направлением обучения.
 *
 * <p>Реализует связь многие-ко-многим между экспертами и направлениями обучения.
 * Определяет, какие направления обслуживаются какими экспертами.
 *
 * <p>Особенности:
 * <ul>
 *   <li>Использует составной первичный ключ (expert_id, direction_id)</li>
 *   <li>Не имеет суррогатного ID - первичный ключ состоит из двух внешних ключей</li>
 *   <li>Обе колонки обязательны (NOT NULL)</li>
 *   <li>Дубликаты пар эксперт-направление не допускаются (обеспечивается составным ключом)</li>
 * </ul>
 *
 * <p>Связи:
 * <ul>
 *   <li>expert: эксперт, связанный с направлением (FK → experts.user_id)</li>
 *   <li>direction: направление обучения (FK → directions.id)</li>
 * </ul>
 *
 * <p>Бизнес-логика:
 * <ul>
 *   <li>Один эксперт может обслуживать несколько направлений</li>
 *   <li>Одно направление может обслуживаться несколькими экспертами</li>
 *   <li>Удаление эксперта или направления должно удалять связанные записи
 *       (через правила внешних ключей БД или логику приложения)</li>
 * </ul>
 *
 * <p>Примечания для будущего расширения:
 * <ul>
 *   <li>Сущность может быть расширена полями: priority, is_active, created_at</li>
 *   <li>Текущая структура позволяет добавить эти поля без рефакторинга</li>
 * </ul>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "expert_directions")
public class ExpertDirection {

    /**
     * Составной первичный ключ.
     *
     * <p>Состоит из expert_id и direction_id.
     * Используется @EmbeddedId для встраивания составного ключа.
     */
    @EmbeddedId
    private ExpertDirectionId id;

    /**
     * Эксперт, связанный с направлением.
     *
     * <p>Связь многие-к-одному с таблицей experts.
     * Внешний ключ: expert_id → experts.user_id
     *
     * <p>@MapsId("expertId") указывает, что значение id.expertId
     * будет автоматически синхронизировано из этого отношения.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("expertId")
    @JoinColumn(name = "expert_id", nullable = false)
    private Expert expert;

    /**
     * Направление обучения.
     *
     * <p>Связь многие-к-одному с таблицей directions.
     * Внешний ключ: direction_id → directions.id
     *
     * <p>@MapsId("directionId") указывает, что значение id.directionId
     * будет автоматически синхронизировано из этого отношения.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("directionId")
    @JoinColumn(name = "direction_id", nullable = false)
    private Direction direction;

    /**
     * Составной идентификатор для связи эксперт-направление.
     *
     * <p>Используется как первичный ключ таблицы expert_directions.
     * Состоит из двух полей:
     * <ul>
     *   <li>expertId: идентификатор эксперта (FK → experts.user_id)</li>
     *   <li>directionId: идентификатор направления (FK → directions.id)</li>
     * </ul>
     *
     * <p>Реализует Serializable для поддержки JPA.
     * Lombok @Data автоматически генерирует equals() и hashCode() на основе обоих полей.
     */
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExpertDirectionId implements Serializable {

        /**
         * Идентификатор эксперта.
         * <p>
         * Внешний ключ к таблице experts (колонка user_id).
         * Обязательное поле.
         */
        @Column(name = "expert_id")
        private Long expertId;

        /**
         * Идентификатор направления обучения.
         * <p>
         * Внешний ключ к таблице directions (колонка id).
         * Обязательное поле.
         */
        @Column(name = "direction_id")
        private Long directionId;
    }
}
