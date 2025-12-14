package team.mephi.adminbot.vaadin.providers;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import team.mephi.adminbot.dto.UserDto;
import team.mephi.adminbot.repository.UserRepository;

public class UserProvider extends CallbackDataProvider<UserDto, String> {
    public UserProvider(UserRepository questionRepository, String role, TextField searchField) {
        super(query -> {
                    // Используем Stream для получения нужного диапазона данных из репозитория
                    // В реальном приложении здесь обычно используется JpaSpecificationExecutor с пагинацией
                    return questionRepository.findAllByRoleAndName(role, searchField.getValue())
                            .stream()
                            .map(u -> new UserDto(u.getId(), u.getUserName(),  u.getLastName() + " " + u.getFirstName(), u.getEmail(), u.getTgName(), u.getPhoneNumber(), u.getPdConsent(), u.getCohort(), u.getDirection() != null ? u.getDirection().getName() : "", u.getCity(), u.getStatus().name()))
                            .skip(query.getOffset()) // Пропускаем уже загруженные элементы
                            .limit(query.getLimit()); // Берем только нужное количество
                },
                // Метод count (подсчет общего количества результатов фильтрации)
                query -> questionRepository.countByRoleAndName(role, searchField.getValue()),
                UserDto::getId
        );
    }
}
