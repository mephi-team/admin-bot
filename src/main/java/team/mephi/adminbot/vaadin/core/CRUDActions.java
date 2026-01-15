package team.mephi.adminbot.vaadin.core;

import team.mephi.adminbot.vaadin.service.DialogType;

import java.util.List;

/**
 * Интерфейс для действий CRUD (Создание, Чтение, Обновление, Удаление) над сущностями типа T.
 *
 * @param <T> Тип сущности, над которой выполняются действия CRUD.
 */
public interface CRUDActions<T> {
    /**
     * Создание нового элемента.
     *
     * @param item  - новый элемент
     * @param type  - тип диалога
     * @param param - дополнительные параметры
     */
    void onCreate(Object item, DialogType type, Object... param);

    /**
     * Просмотр элемента.
     *
     * @param item - элемент для просмотра
     * @param type - тип диалога
     */
    void onView(T item, DialogType type);

    /**
     * Редактирование элемента.
     *
     * @param item  - элемент для редактирования
     * @param type  - тип диалога
     * @param param - дополнительные параметры
     */
    void onEdit(T item, DialogType type, Object... param);

    /**
     * Удаление элементов по их идентификаторам.
     *
     * @param ids   - список идентификаторов элементов для удаления
     * @param type  - тип диалога
     * @param param - дополнительные параметры
     */
    void onDelete(List<Long> ids, DialogType type, Object... param);
}
