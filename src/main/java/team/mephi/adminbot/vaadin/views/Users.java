package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabSheetVariant;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;
import team.mephi.adminbot.service.UserService;
import team.mephi.adminbot.vaadin.components.UserCountBadge;
import team.mephi.adminbot.vaadin.components.buttons.PrimaryButton;
import team.mephi.adminbot.vaadin.components.buttons.SecondaryButton;
import team.mephi.adminbot.vaadin.core.CRUDActions;
import team.mephi.adminbot.vaadin.service.DialogType;
import team.mephi.adminbot.vaadin.users.components.FileUploadDialog;
import team.mephi.adminbot.vaadin.users.components.FileUploadDialogFactory;
import team.mephi.adminbot.vaadin.users.presenter.UsersPresenterFactory;
import team.mephi.adminbot.vaadin.users.tabs.UserTabProvider;
import team.mephi.adminbot.vaadin.users.tabs.UserTabType;

import java.util.*;

/**
 * Представление страницы пользователей с вкладками для различных ролей пользователей.
 * Доступно только пользователям с ролью ADMIN.
 */
@Route("/users")
@RolesAllowed("ADMIN")
public class Users extends VerticalLayout implements BeforeEnterObserver {
    private final FileUploadDialog fileUploadDialog;

    private final TabSheet tabSheet = new TabSheet();
    private final List<UserTabType> rolesInOrder = new ArrayList<>();
    private final Map<Object, CRUDActions<?>> actions = new HashMap<>();

    private UserTabType currentTab;

    /**
     * Конструктор для создания представления страницы пользователей.
     *
     * @param tabProviders     список провайдеров вкладок пользователей.
     * @param presenterFactory фабрика для создания презентеров пользователей.
     * @param uploaderFactory  фабрика для создания диалогов загрузки файлов.
     * @param userService      сервис для работы с пользователями.
     */
    public Users(
            List<UserTabProvider> tabProviders,
            UsersPresenterFactory presenterFactory,
            FileUploadDialogFactory uploaderFactory,
            UserService userService
    ) {
        this.fileUploadDialog = uploaderFactory.create();
        getElement().getStyle().set("padding-inline", "120px");
        setSizeFull();
        tabSheet.setSizeFull();
        tabSheet.addThemeVariants(TabSheetVariant.LUMO_TABS_MINIMAL);
        add(createHeader(), tabSheet);

        tabProviders.sort(Comparator.comparingInt(UserTabProvider::getPosition));

        // Создаём вкладки
        for (var provider : tabProviders) {
            var tabId = provider.getTabId();
            var presenter = presenterFactory.createPresenter(tabId);
            var content = provider.createTabContent(presenter);

            rolesInOrder.add(tabId);
            actions.put(tabId, presenter);

            var userCount = userService.getAllCounts().getOrDefault(provider.getTabId().name(), 0L);
            Span tabContent = new Span(new Span(getTranslation(provider.getTabLabel())), new UserCountBadge(userCount));
            var tab = new Tab(tabContent);
            tabSheet.add(tab, content, provider.getPosition());
        }
        tabSheet.addSelectedChangeListener(ignoredEvent -> {
            var selectedTab = tabSheet.getSelectedIndex();
            if (selectedTab > -1) {
                currentTab = rolesInOrder.get(selectedTab);
                UI.getCurrent().navigate(getClass(), QueryParameters.of("tab", currentTab.name().toLowerCase()));
            }
        });
    }

    /**
     * Создание заголовка страницы с кнопками создания пользователя и загрузки из файла.
     *
     * @return горизонтальный макет заголовка.
     */
    private HorizontalLayout createHeader() {
        HorizontalLayout top = new HorizontalLayout();
        top.setWidthFull();
        top.addToStart(new H1(getTranslation("page_users_title")));

        var secondaryButton = new SecondaryButton(getTranslation("page_users_create_from_file_button"), VaadinIcon.FILE_ADD.create(), ignoredEvent -> fileUploadDialog.open());
        var primaryButton = new PrimaryButton(getTranslation("page_users_create_user_button"), VaadinIcon.PLUS.create(), ignoredEvent -> getCurrentAction().onCreate(getCurrentRole().name(), getCreateDialogType()));
        Div buttons = new Div(secondaryButton, primaryButton);
        buttons.addClassNames(LumoUtility.Display.FLEX, LumoUtility.Gap.MEDIUM);
        top.addToEnd(buttons);
        return top;
    }

    /**
     * Получение текущей роли на основе выбранной вкладки.
     *
     * @return текущий тип вкладки пользователя.
     */
    private UserTabType getCurrentRole() {
        return currentTab;
    }

    /**
     * Получение типа диалога создания на основе текущей роли.
     *
     * @return тип диалога создания.
     */
    private DialogType getCreateDialogType() {
        UserTabType role = getCurrentRole();
        // Простой маппинг ролей табов в DialogType; при необходимости расширить
        return switch (role) {
            case TUTOR -> DialogType.TUTORS_CREATED;
            default -> DialogType.USERS_CREATED;
        };
    }

    /**
     * Получение текущих действий CRUD на основе выбранной вкладки.
     *
     * @return текущие действия CRUD.
     */
    private CRUDActions<?> getCurrentAction() {
        return actions.get(getCurrentRole());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        var currentTabText = event.getLocation().getQueryParameters().getSingleParameter("tab").orElse(UserTabType.VISITOR.name());
        currentTab = UserTabType.valueOf(currentTabText.toUpperCase());
        tabSheet.setSelectedIndex(rolesInOrder.indexOf(currentTab));
    }
}
