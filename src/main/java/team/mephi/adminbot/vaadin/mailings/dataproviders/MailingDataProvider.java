package team.mephi.adminbot.vaadin.mailings.dataproviders;

import com.vaadin.flow.data.provider.DataProvider;

public interface MailingDataProvider {
    DataProvider<?, ?> getDataProvider();
    void deleteAllById(Iterable<Long> ids);
    void refresh();
}
