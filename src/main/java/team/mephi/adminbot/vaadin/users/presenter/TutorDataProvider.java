package team.mephi.adminbot.vaadin.users.presenter;

import team.mephi.adminbot.dto.SimpleTutor;
import team.mephi.adminbot.vaadin.core.CRUDDataProvider;

/**
 * Провайдер данных для работы с репозиторием преподавателей.
 * Расширяет общий CRUD-провайдер и добавляет метод для блокировки нескольких преподавателей по их идентификаторам.
 */
public interface TutorDataProvider extends CRUDDataProvider<SimpleTutor> {
    /**
     * Блокирует всех преподавателей с указанными идентификаторами.
     *
     * @param ids Идентификаторы преподавателей для блокировки.
     */
    void blockAllById(Iterable<Long> ids);
}