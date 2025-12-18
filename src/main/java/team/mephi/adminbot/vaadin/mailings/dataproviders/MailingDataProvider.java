package team.mephi.adminbot.vaadin.mailings.dataproviders;

import com.vaadin.flow.data.provider.DataProvider;

import java.util.Optional;

public interface MailingDataProvider <T> {
    DataProvider<?, ?> getDataProvider();
    Optional<T> findById(Long id);
    T save(T user);
    void deleteAllById(Iterable<Long> ids);
    void refresh();
}
