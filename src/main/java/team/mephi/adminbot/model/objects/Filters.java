package team.mephi.adminbot.model.objects;

import lombok.Builder;
import lombok.Data;

/**
 * Класс Filters представляет собой набор фильтров для поиска или сортировки данных.
 * Содержит поля для пользователей, когорты, направления, города и куратора.
 */
@Data
@Builder
public class Filters {
    private String users;
    private String cohort;
    private String direction;
    private String city;
    private String curator;
}
