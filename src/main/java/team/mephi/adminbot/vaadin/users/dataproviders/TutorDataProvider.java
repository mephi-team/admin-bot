package team.mephi.adminbot.vaadin.users.dataproviders;

import com.vaadin.flow.data.provider.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.dto.TutorWithCounts;
import team.mephi.adminbot.model.Tutor;
import team.mephi.adminbot.repository.TutorRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TutorDataProvider implements UserDataProvider {

    private final TutorRepository tutorRepository;

    private ConfigurableFilterDataProvider<TutorWithCounts, Void, String> provider;

    public TutorDataProvider(TutorRepository tutorRepository) {
        this.tutorRepository = tutorRepository;
    }

    public ConfigurableFilterDataProvider<TutorWithCounts, Void, String> getFilterableProvider() {
        if (provider == null) {
            CallbackDataProvider<TutorWithCounts, String> base = new CallbackDataProvider<>(
                    query -> {
                        List<QuerySortOrder> sortOrders = query.getSortOrders();
                        Sort sort = Sort.by(sortOrders.stream()
                                .map(so -> so.getDirection() == SortDirection.ASCENDING
                                        ? Sort.Order.asc(so.getSorted())
                                        : Sort.Order.desc(so.getSorted()))
                                .collect(Collectors.toList()));
                        Pageable pageable = PageRequest.of(
                                query.getOffset() / query.getLimit(),
                                query.getLimit(),
                                sort
                        );
                        return tutorRepository.findAllWithDirectionsAndStudents(query.getFilter().orElse(""), pageable)
                            .stream()
                            .skip(query.getOffset())
                            .limit(query.getLimit());
                        },
                    query -> tutorRepository.countByName(query.getFilter().orElse("")),
                    TutorWithCounts::getId
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
    public Optional<SimpleUser> findById(Long id) {
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
    public void blockAllById(Iterable<Long> ids) {
        tutorRepository.blockAllById(ids);
    }
}