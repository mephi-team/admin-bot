package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import team.mephi.adminbot.dto.RoleDto;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.repository.RoleRepository;
import team.mephi.adminbot.repository.TutorRepository;
import team.mephi.adminbot.repository.UserRepository;
import team.mephi.adminbot.vaadin.components.UserCountBadge;
import team.mephi.adminbot.vaadin.components.UserConfirmDialog;
import team.mephi.adminbot.vaadin.components.RightDrawer;
import team.mephi.adminbot.vaadin.components.UserForm;
import team.mephi.adminbot.vaadin.providers.ProviderGet;
import team.mephi.adminbot.vaadin.views.users.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Route("/users")
@RolesAllowed("ADMIN")
public class Users extends VerticalLayout {
    private final String BLOCK_TITLE = "Блокировать пользователя?";
    private final String BLOCK_TEXT = "Вы действительно хотите заблокировать пользователя?";
    private final String BLOCK_MESSAGE = "Пользователь заблокирован";
    private final String BLOCK_ALL_TITLE = "Блокировать пользователей?";
    private final String BLOCK_ALL_TEXT = "Вы действительно хотите заблокировать %d пользователей?";
    private final String BLOCK_ALL_MESSAGE = "Заблокировано %d пользователей";
    private final String BLOCK_ACTION = "Блокировать";
    private final String ACCEPT_TITLE = "Утвердить кандидата?";
    private final String ACCEPT_TEXT = "Вы действительно хотите утвердить кандидата?";
    private final String ACCEPT_MESSAGE = "Информация о приеме отправлена";
    private final String ACCEPT_ALL_TITLE = "Утвердить кандидатов?";
    private final String ACCEPT_ALL_TEXT = "Вы действительно хотите утвердить %d кандидатов?";
    private final String ACCEPT_ALL_MESSAGE = "Информация о приеме отправлена %d кандидатам";
    private final String ACCEPT_ACTION = "Утвердить";
    private final String REJECT_TITLE = "Отклонить кандидата?";
    private final String REJECT_TEXT = "Вы действительно хотите отклонить кандидата?";
    private final String REJECT_MESSAGE = "Информация об отказе отправлена кандидату";
    private final String REJECT_ALL_TITLE = "Отклонить кандидатов?";
    private final String REJECT_ALL_TEXT = "Вы действительно хотите отклонить %d кандидатов?";
    private final String REJECT_ALL_MESSAGE = "Информация об отказе отправлена %d кандидатам";
    private final String REJECT_ACTION = "Отклонить";

    RightDrawer driver;
    UserConfirmDialog dialogBlock = new UserConfirmDialog(BLOCK_TITLE, BLOCK_TEXT, BLOCK_ACTION, BLOCK_ALL_TITLE, BLOCK_ALL_TEXT, BLOCK_MESSAGE, BLOCK_ALL_MESSAGE, this::showBlockMessage);
    UserConfirmDialog dialogAccept = new UserConfirmDialog(ACCEPT_TITLE, ACCEPT_TEXT, ACCEPT_ACTION, ACCEPT_ALL_TITLE, ACCEPT_ALL_TEXT, ACCEPT_MESSAGE, ACCEPT_ALL_MESSAGE, this::showMessage);
    UserConfirmDialog dialogReject = new UserConfirmDialog(REJECT_TITLE, REJECT_TEXT, REJECT_ACTION, REJECT_ALL_TITLE, REJECT_ALL_TEXT, REJECT_MESSAGE, REJECT_ALL_MESSAGE,  this::showMessage);
    Map<String, Long> roleCounts;
    List<Component> tables;
    TabSheet tabSheet;
    ProviderGet provider;
    List<Long> blockIds;

    private final BeanValidationBinder<SimpleUser> binder = new BeanValidationBinder<>(SimpleUser.class);

    public Users(UserRepository userRepository, TutorRepository tutorRepository, RoleRepository roleRepository) {
        List<String> tabs = List.of("visitor", "candidate", "middle_candidate", "student", "free_listener", "lc_expert", "tutor");
        List<String> tabNames = List.of("Гости", "Кандидаты", "Миддл-Кандидаты", "Студенты", "Слушатели", "Эксперты", "Кураторы");

        setHeightFull();
        HorizontalLayout top = new HorizontalLayout();
        top.setWidthFull();
        top.addToStart(new H1("Пользователи"));
        var primaryButton = new Button("Добавить пользователя", new Icon(VaadinIcon.PLUS), e -> {
            var selectedTab = tabSheet.getSelectedIndex();
            var user = new SimpleUser();
            user.setRole(tabs.get(selectedTab));
            binder.setBean(user);
            provider = (ProviderGet) tables.get(selectedTab);
            driver.setVisible(true);
        });
        primaryButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Div buttons = new Div(new Button("Загрузить из файла", new Icon(VaadinIcon.FILE_ADD)), primaryButton);
        buttons.getElement().getStyle().set("display", "flex");
        buttons.getElement().getStyle().set("gap", "24px");
        top.addToEnd(buttons);

        tabSheet = new TabSheet();
        tabSheet.setSizeFull();

        roleCounts = userRepository.countsByRole();

        Map<String, UserCountBadge> badges = tabs.stream().collect(Collectors.toMap(
                s -> s, key -> new UserCountBadge(roleCounts.getOrDefault(key, 0L))));

        tables = List.of(
                new GuestsView(userRepository, roleRepository, "visitor", this::onView, this::onBlock),
                new CandidateView(userRepository, roleRepository, "candidate", this::onView, this::onEdit, this::onBlock, this::onAccept, this::onReject),
                new MiddleCandidateView(userRepository, roleRepository, "middle_candidate", this::onView, this::onEdit, this::onBlock),
                new StudentView(userRepository, roleRepository, "student", this::onView, this::onEdit, this::onBlock),
                new FreeListenerView(userRepository, roleRepository, "free_listener", this::onView, this::onEdit, this::onBlock),
                new ExpertsView(userRepository, roleRepository, "lc_expert", this::onView, this::onEdit, this::onBlock),
                new TutorsView(tutorRepository, this::onView, this::onEdit, this::onBlock)
        );

        tabs.forEach(tab -> {
            int index = tabs.indexOf(tab);
            tabSheet.add(new Span(new Span(tabNames.get(index)), badges.get(tab)), tables.get(index));
        });

        List<RoleDto> roles = roleRepository.findAll().stream()
                .map(r -> new RoleDto(r.getCode(), r.getName(), r.getDescription()))
                .toList();
        UserForm form = new UserForm(roles);
        driver = new RightDrawer("Редактировать пользователя", form, this::onSave, this::onClose);

        binder.bindInstanceFields(form);
        binder.forField(form.getRoles())
                .withConverter(RoleDto::getCode, code -> roleRepository.findByCode(code).map(a -> new RoleDto(a.getCode(), a.getName(), a.getDescription())).orElseThrow())
                .bind(SimpleUser::getRole, SimpleUser::setRole);

        add(top, tabSheet, driver);
    }

    private void showMessage(String message) {
        Notification.show(message, 3000, Notification.Position.TOP_END);
    }

    private void showBlockMessage(String message) {
        provider.deleteAllById(blockIds);
        provider.refreshAll();
        Notification.show(message, 3000, Notification.Position.TOP_END);
        blockIds = null;
        this.provider = null;
    }

    private void onAccept(List<Long> ids) {
        dialogAccept.setCount(ids.size());
        dialogAccept.open();
    }

    private void onReject(List<Long> ids) {
        dialogReject.setCount(ids.size());
        dialogReject.open();
    }

    private Boolean onSave() {
        if (binder.validate().isOk()) {
            provider.save(binder.getBean());
            provider.refreshAll();
            this.provider = null;
            Notification.show("Сохранено", 3000, Notification.Position.TOP_END);
            return true;
        }
        return false;
    }

    private void onView(Long id, ProviderGet provider) {
        this.provider = provider;
        Optional<SimpleUser> simpleUser = provider.findSimpleUserById(id);
        simpleUser.ifPresent(u -> {
            binder.setBean(u);
            driver.setVisible(true);
        });
    }

    private void onEdit(Long id, ProviderGet provider) {
        this.provider = provider;
        Optional<SimpleUser> simpleUser = provider.findSimpleUserById(id);
        simpleUser.ifPresent(u -> {
            binder.setBean(u);
            driver.setVisible(true);
        });
    }

    private void onBlock(List<Long> ids, ProviderGet provider) {
        this.provider = provider;
        dialogBlock.setCount(ids.size());
        dialogBlock.open();
        blockIds = ids;
    }

    void onClose() {
        this.provider = null;
        binder.setBean(new SimpleUser());
    }

}
