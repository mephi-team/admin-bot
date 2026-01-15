package team.mephi.adminbot.vaadin.users.presenter;

import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.vaadin.CRUDDataProvider;

/**
 * Провайдер данных для работы с репозиторием пользователей.
 * Расширяет общий CRUD-провайдер и добавляет метод для блокировки нескольких пользователей по их идентификаторам.
 */
public interface UserDataProvider extends CRUDDataProvider<SimpleUser> {
    /**
     * Блокирует всех пользователей с указанными идентификаторами.
     *
     * @param ids Идентификаторы пользователей для блокировки.
     */
    void blockAllById(Iterable<Long> ids);
}