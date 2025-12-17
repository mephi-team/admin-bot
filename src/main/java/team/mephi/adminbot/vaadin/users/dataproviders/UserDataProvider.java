package team.mephi.adminbot.vaadin.users.dataproviders;

import com.vaadin.flow.data.provider.DataProvider;
import team.mephi.adminbot.dto.SimpleUser;

import java.util.Optional;

public interface UserDataProvider {
    DataProvider<?, ?> getDataProvider();
    Optional<SimpleUser> findUserById(Long id);
    SimpleUser save(SimpleUser user);
    void deleteAllById(Iterable<Long> ids);
    void refresh();
}