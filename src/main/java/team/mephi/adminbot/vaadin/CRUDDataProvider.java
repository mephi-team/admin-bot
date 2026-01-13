package team.mephi.adminbot.vaadin;

import com.vaadin.flow.data.provider.DataProvider;

import java.util.Optional;

public interface CRUDDataProvider<T> {
    DataProvider<T, ?> getDataProvider();

    Optional<T> findById(Long id);

    T save(T item);

    void deleteAllById(Iterable<Long> ids);
}
