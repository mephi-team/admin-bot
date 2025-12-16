package team.mephi.adminbot.vaadin.providers;

import com.vaadin.flow.data.provider.DataProvider;
import org.springframework.data.repository.CrudRepository;
import team.mephi.adminbot.dto.SimpleUser;

import java.util.Optional;

public interface ProviderGet {
    DataProvider<?, ?> getProvider();

    CrudRepository<?, Long> getRepository();

    Optional<SimpleUser> findSimpleUserById(Long id);

    SimpleUser save(SimpleUser user);

    void deleteById(Long id);

    void refreshAll();
}
