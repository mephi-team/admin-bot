package team.mephi.adminbot.vaadin.core;

import com.vaadin.flow.data.provider.DataProvider;

import java.util.Optional;

/**
 * Интерфейс для провайдера данных с поддержкой операций CRUD (Создание, Чтение, Обновление, Удаление) над сущностями типа T.
 *
 * @param <T> Тип сущности, с которой работает провайдер данных.
 */
public interface CRUDDataProvider<T> {
    /**
     * Получает провайдер данных для работы с сущностями типа T.
     *
     * @return Провайдер данных.
     */
    DataProvider<T, ?> getDataProvider();

    /**
     * Находит сущность по ее идентификатору.
     *
     * @param id Идентификатор сущности.
     * @return Опциональная сущность, если она найдена.
     */
    Optional<T> findById(Long id);

    /**
     * Сохраняет сущность.
     *
     * @param item Сущность для сохранения.
     * @return Сохраненная сущность.
     */
    T save(T item);

    /**
     * Удаляет сущности по их идентификаторам.
     *
     * @param ids Идентификаторы сущностей для удаления.
     */
    void deleteAllById(Iterable<Long> ids);
}
