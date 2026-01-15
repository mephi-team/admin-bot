package team.mephi.adminbot.vaadin;

import com.vaadin.flow.data.provider.DataProvider;

import java.util.Optional;

/**
 * Интерфейс для провайдера данных с поддержкой операций CRUD (Создание, Чтение, Обновление, Удаление) над сущностями типа T.
 *
 * @param <T> Тип сущности, с которой работает провайдер данных.
 */
public interface CRUDDataProvider<T> {
    DataProvider<T, ?> getDataProvider();

    Optional<T> findById(Long id);

    T save(T item);

    void deleteAllById(Iterable<Long> ids);
}
