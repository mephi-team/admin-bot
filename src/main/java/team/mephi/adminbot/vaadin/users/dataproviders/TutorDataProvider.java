package team.mephi.adminbot.vaadin.users.dataproviders;

import com.vaadin.flow.data.provider.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.model.Tutor;
import team.mephi.adminbot.repository.TutorRepository;
import team.mephi.adminbot.vaadin.users.presenter.UserDataProvider;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TutorDataProvider implements UserDataProvider {

    private final TutorRepository tutorRepository;

    private ConfigurableFilterDataProvider<SimpleUser, Void, String> provider;

    public TutorDataProvider(TutorRepository tutorRepository) {
        this.tutorRepository = tutorRepository;
    }

    public ConfigurableFilterDataProvider<SimpleUser, Void, String> getFilterableProvider() {
        if (provider == null) {
            CallbackDataProvider<SimpleUser, String> base = new CallbackDataProvider<>(
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
                                sort.isUnsorted() ? Sort.by("id").descending() : sort
                        );
                        return tutorRepository.findAllWithDirectionsAndStudents(query.getFilter().orElse(""), pageable)
                            .stream()
                                .map(u -> SimpleUser.builder()
                                        .id(u.getId())
                                        .role("tutor")
                                        .fullName(u.getLastName() + " " + u.getFirstName())
                                        .firstName(u.getFirstName())
                                        .lastName(u.getLastName())
                                        .email(u.getEmail())
                                        .tgId(u.getTgId())
                                        .studentCount(u.getStudentAssignments().size())
                                        .status("ACTIVE")
                                        .build());
                        },
                    query -> tutorRepository.countByName(query.getFilter().orElse("")),
                    SimpleUser::getId
            );
            provider = base.withConfigurableFilter();
        }
        return provider;
    }

    @Override
    public DataProvider<SimpleUser, ?> getDataProvider() {
        return getFilterableProvider();
    }

    @Override
    public Optional<SimpleUser> findById(Long id) {
        return tutorRepository.findSimpleUserById(id).map(t -> SimpleUser.builder()
                .id(t.getId())
                .role("tutor")
                .firstName(t.getFirstName())
                .lastName(t.getLastName())
                .email(t.getEmail())
                .phoneNumber(t.getPhone())
                .tgId(t.getTgId())
                .tgName(t.getTgName())
                .build());
    }

    @Override
    public SimpleUser save(SimpleUser dto) {
        Tutor user = dto.getId() != null
                ? tutorRepository.findById(dto.getId()).orElse(new Tutor())
                : new Tutor();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setTgId(dto.getTgId());
        user = tutorRepository.save(user);
        return new SimpleUser();
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