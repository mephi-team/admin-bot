package team.mephi.adminbot.vaadin.users.dataproviders;

import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import org.springframework.stereotype.Component;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.dto.TutorWithCounts;
import team.mephi.adminbot.model.Tutor;
import team.mephi.adminbot.repository.RoleRepository;
import team.mephi.adminbot.repository.TutorRepository;

import java.util.Optional;

@Component("tutor")
public class TutorDataProvider implements UserDataProvider {

    private final TutorRepository tutorRepository;
    private final RoleRepository roleRepository;

    private ConfigurableFilterDataProvider<TutorWithCounts, Void, String> provider;

    public TutorDataProvider(TutorRepository tutorRepository, RoleRepository roleRepository) {
        this.tutorRepository = tutorRepository;
        this.roleRepository = roleRepository;
    }

    public ConfigurableFilterDataProvider<TutorWithCounts, Void, String> getFilterableProvider() {
        if (provider == null) {
            CallbackDataProvider<TutorWithCounts, String> base = new CallbackDataProvider<>(
                    query -> tutorRepository.findAllWithDirectionsAndStudents(query.getFilter().orElse(""))
                            .stream()
                            .skip(query.getOffset())
                            .limit(query.getLimit()),
                    query -> tutorRepository.countByName(query.getFilter().orElse(""))
            );
            provider = base.withConfigurableFilter();
        }
        return provider;
    }

    @Override
    public DataProvider<TutorWithCounts, ?> getDataProvider() {
        return getFilterableProvider();
    }

    @Override
    public Optional<SimpleUser> findUserById(Long id) {
        return tutorRepository.findSimpleUserById(id);
    }

    @Override
    public SimpleUser save(SimpleUser dto) {
        Tutor user = dto.getId() != null
                ? tutorRepository.findById(dto.getId()).orElse(new Tutor())
                : new Tutor();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setTgId(dto.getTelegram());
        user = tutorRepository.save(user);
        return new SimpleUser(user.getId(), "tutor", user.getFirstName(), user.getLastName(), user.getEmail(), user.getTgId());
    }

    @Override
    public void deleteAllById(Iterable<Long> ids) {
        tutorRepository.deleteAllById(ids);
    }

    @Override
    public void refresh() {
        if (provider != null) {
            provider.refreshAll();
        }
    }
}