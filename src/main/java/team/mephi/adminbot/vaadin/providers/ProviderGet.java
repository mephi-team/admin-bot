package team.mephi.adminbot.vaadin.providers;

import com.vaadin.flow.data.provider.DataProvider;
import org.springframework.data.repository.CrudRepository;

public interface ProviderGet {
    DataProvider<?, ?> getProvider();
    CrudRepository<?, Long> getRepository();
}
