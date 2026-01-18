package team.mephi.adminbot.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.mephi.adminbot.dto.*;
import team.mephi.adminbot.model.*;
import team.mephi.adminbot.model.enums.StudentTutorMode;
import team.mephi.adminbot.model.enums.UserStatus;
import team.mephi.adminbot.repository.TutorRepository;
import team.mephi.adminbot.repository.UserRepository;

import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

/**
 * Сервис для управления пользователями.
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TutorRepository tutorRepository;

    private final List<UserDto> curators = new ArrayList<>(List.of(UserDto.builder().userName("Все").build()));

    /**
     * Конструктор для внедрения зависимостей.
     *
     * @param userRepository  репозиторий для управления пользователями.
     * @param tutorRepository репозиторий для управления репетиторами.
     */
    public UserServiceImpl(UserRepository userRepository, TutorRepository tutorRepository) {
        this.userRepository = userRepository;
        this.tutorRepository = tutorRepository;
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(u -> UserDto.builder()
                        .id(u.getId())
                        .userName(u.getUserName())
                        .tgName(u.getTgId())
                        .build()
                )
                .toList();
    }

    @Override
    public List<UserDto> getAllUsers(Pageable pageable, String query) {
        return userRepository.searchAll(query)
                .stream()
                .map(u -> UserDto.builder().id(u.getId()).userName(u.getUserName()).build())
                .skip(pageable.getOffset()) // Пропускаем уже загруженные элементы
                .limit(pageable.getPageSize())
                .toList();
    }

    @Override
    public Optional<UserDto> getById(Long id) {
        return userRepository.findById(id).map(u -> UserDto.builder().id(u.getId()).userName(u.getUserName()).build());
    }

    @Override
    public Map<String, Long> getAllCounts() {
        return userRepository.countsByRole();
    }

    @Override
    public Optional<SimpleUser> findById(Long id) {
        return userRepository.findByIdWithRoleAndDirection(id)
                .map(this::mapToSimple);
    }

    @Transactional
    @Override
    public SimpleUser save(SimpleUser dto) {
        User user = dto.getId() != null
                ? userRepository.findById(dto.getId()).orElse(new User())
                : new User();

        user.setRole(Role.builder().code(dto.getRole()).build());
        user.setUserName(dto.getFirstName() + " " + dto.getLastName());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setTgId(dto.getTgId());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setCity(dto.getCity());
        var directions = dto.getDirection().stream().findFirst().orElse(SimpleDirection.builder().build());
        user.setDirection(Direction.builder().id(directions.getId()).name(directions.getName()).build());
        user.setCohort(dto.getCohort());
        var currentAssignment = user.getTutorAssignments();
        if (Objects.nonNull(dto.getTutor()) && Objects.nonNull(dto.getTutor().getId()) && currentAssignment.stream().noneMatch(a -> a.getIsActive() && a.getTutor().getId().equals(dto.getTutor().getId()))) {
            user.getTutorAssignments().forEach(ta -> ta.setIsActive(false));
            user.getTutorAssignments().add(StudentTutor.builder()
                    .student(user)
                    .mode(currentAssignment.isEmpty() ? StudentTutorMode.INITIAL : StudentTutorMode.REASSIGN)
                    .isActive(true)
                    .tutor(Tutor.builder().id(dto.getTutor().getId()).build())
                    .assignedAt(Instant.now())
                    .build());
        }

        if (Objects.isNull(user.getStatus())) {
            user.setStatus(UserStatus.ACTIVE);
        }

        user = userRepository.save(user);

        return mapToSimple(user);
    }

    @Override
    public void deleteAllById(Iterable<Long> ids) {
        userRepository.deleteAllById(ids);
    }

    @Override
    public void blockAllById(Iterable<Long> ids) {
        userRepository.blockAllById(ids);
    }

    @Override
    public Stream<SimpleUser> findAllByRoleAndName(String role, String query, Pageable pageable) {
        return userRepository.findAllByRoleAndName(role, query, pageable)
                .stream()
                .map(this::mapToSimple);
    }

    @Override
    @SuppressWarnings("unused")
    public Stream<SimpleUser> findAllByRoleCodeLikeAndCohortLikeAndDirectionCodeLikeAndCityLike(String role, String cohort, Long direction, String city, Long tutor, Pageable pageable) {
        return userRepository.findAllByRoleCodeLikeAndCohortLikeAndDirectionCodeLikeAndCityLike(role, cohort, direction, city, tutor)
                .stream()
                .map(user -> SimpleUser.builder()
                        .id(user.getId())
                        .fullName(user.getUserName())
                        .tgId(user.getTgId())
                        .build());
    }

    @Override
    public Integer countByRoleAndName(String role, String query) {
        return userRepository.countByRoleAndName(role, query);
    }

    @Override
    @SuppressWarnings("unused")
    public List<UserDto> findAllCurators(Pageable pageable, String s) {
        if (curators.size() < 2) initCurators();
        return curators;
    }

    @Override
    public Optional<UserDto> findCuratorByUserName(String name) {
        if (curators.size() < 2) initCurators();
        return curators.stream().filter(c -> c.getUserName().equals(name)).findAny();
    }

    @Override
    public Stream<SimpleUser> findAllForCuratorship(String name, Pageable pageable) {
        var res = userRepository.findAllStudentsWithTutorAssignments(name, "STUDENT", pageable);
        return res.isEmpty()
                ? Stream.of(SimpleUser.builder().fullName("Нет элементов для выбора").build())
                : res.stream().map(
                u -> SimpleUser.builder()
                        .id(u.getId())
                        .fullName(u.getUserName())
                        .firstName(u.getFirstName())
                        .lastName(u.getLastName())
                        .tgId(u.getTgId())
                        .build());
    }

    @Override
    public Integer countAllForCuratorship(String name) {
        var count = userRepository.countAllStudentsWithTutorAssignments(name, "STUDENT");
        return count > 0 ? count : 1;
    }

    @Override
    public Boolean existsById(Long id) {
        return userRepository.existsByIdAndDeletedIsFalse(id);
    }

    @Override
    public List<SimpleUser> findAll() {
        return userRepository.findAllWithDirection().stream().map(this::mapToSimple).toList();
    }

    @Override
    public Long countAllUsers() {
        return userRepository.countByDeletedIsFalse();
    }

    /**
     * Инициализация списка кураторов.
     */
    private void initCurators() {
        curators.addAll(tutorRepository.findAll().stream().map(u -> UserDto.builder()
                .id(u.getId())
                .userName(u.getUserName())
                .build()).toList());
    }

    /**
     * Преобразует объект User в SimpleUser.
     *
     * @param user объект User для преобразования.
     * @return преобразованный объект SimpleUser.
     */
    private SimpleUser mapToSimple(User user) {
        var tutor = user.getTutorAssignments().stream().filter(StudentTutor::getIsActive).findAny().orElseGet(() -> StudentTutor.builder().tutor(Tutor.builder().userName("").firstName("").lastName("").build()).build()).getTutor();
        return SimpleUser.builder()
                .id(user.getId())
                .role(user.getRole().getCode())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getUserName())
                .phoneNumber(user.getPhoneNumber())
                .tgId(user.getTgId())
                .tgName(user.getTgName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .pdConsent(user.getPdConsent())
                .pdConsentLog(user.getPdConsentLogs().stream().map(pd -> SimplePd.builder().id(pd.getId()).source(pd.getSource()).status(pd.getStatus().name()).build()).toList())
                .status(user.getStatus().name())
                .city(user.getCity())
                .direction(Objects.nonNull(user.getDirection()) ? Set.of(SimpleDirection.builder().id(user.getDirection().getId()).name(user.getDirection().getName()).build()) : null)
                .cohort(user.getCohort())
                .tutor((tutor == null || tutor.getId() == null) ? null : TutorDto.builder().id(tutor.getId()).fullName(tutor.getLastName() + " " + tutor.getFirstName()).build())
                .build();
    }
}
