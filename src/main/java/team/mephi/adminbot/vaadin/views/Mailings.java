package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabSheetVariant;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.components.UserCountBadge;
import team.mephi.adminbot.vaadin.components.buttons.PrimaryButton;
import team.mephi.adminbot.vaadin.components.layout.DialogsLayout;
import team.mephi.adminbot.vaadin.mailings.presenter.MailingPresenterFactory;
import team.mephi.adminbot.vaadin.mailings.service.MailingCountService;
import team.mephi.adminbot.vaadin.mailings.tabs.MailingTabProvider;
import team.mephi.adminbot.vaadin.mailings.tabs.MailingTabType;
import team.mephi.adminbot.vaadin.service.DialogType;

import java.util.*;

/**
 * Представление страницы рассылок с вкладками для различных типов рассылок.
 * Доступно только пользователям с ролью ADMIN.
 */
@Route(value = "/mailings", layout = DialogsLayout.class)
@RolesAllowed("ADMIN")
public class Mailings extends VerticalLayout {
    private final TabSheet tabSheet = new TabSheet();
    private final Button primaryButton = new PrimaryButton(getTranslation("page_mailing_create_mailing_button"), VaadinIcon.PLUS.create());

    private final List<MailingTabType> rolesInOrder = new ArrayList<>();
    private final Map<MailingTabType, CRUDActions<?>> actions = new HashMap<>();

    /**
     * Конструктор для создания представления страницы рассылок.
     *
     * @param tabProviders       список провайдеров вкладок рассылок.
     * @param presenterFactory   фабрика для создания презентеров рассылок.
     * @param mailingCountService сервис для получения количества рассылок по типам.
     */
    public Mailings(
            List<MailingTabProvider> tabProviders,
            MailingPresenterFactory presenterFactory,
            MailingCountService mailingCountService
    ) {
        setHeightFull();
        getElement().getStyle().set("padding-inline", "53px 120px");
        tabSheet.setSizeFull();
        tabSheet.addThemeVariants(TabSheetVariant.LUMO_TABS_MINIMAL);
        add(createHeader(), tabSheet);

        tabProviders.sort(Comparator.comparingInt(MailingTabProvider::getPosition));

        for (var provider : tabProviders) {
            var tabId = provider.getTabId();
            var presenter = presenterFactory.createPresenter(tabId);
            var content = provider.createTabContent(presenter);

            rolesInOrder.add(tabId);
            actions.put(tabId, presenter);

            var userCount = mailingCountService.getAllCounts().getOrDefault(provider.getTabId().name(), 0L);
            Span tabContent = new Span(new Span(getTranslation(provider.getTabLabel())), new UserCountBadge(userCount));
            var tab = new Tab(tabContent);
            tabSheet.add(tab, content, provider.getPosition());
        }
        tabSheet.addSelectedChangeListener(ignoredEvent -> {
            if (tabSheet.getSelectedIndex() == 1) {
                primaryButton.setText(getTranslation("page_mailing_create_template_button"));
            } else {
                primaryButton.setText(getTranslation("page_mailing_create_mailing_button"));
            }
        });
    }

    // Создание заголовка страницы с кнопкой создания рассылки
    private HorizontalLayout createHeader() {
        HorizontalLayout top = new HorizontalLayout();
        top.setWidthFull();
        top.addToStart(new H1(getTranslation("page_mailing_title")));

        primaryButton.addClickListener(ignoredEvent -> getCurrentAction().onCreate(null, getCreateDialogType()));
        top.addToEnd(primaryButton);
        return top;
    }

    // Получение текущей роли на основе выбранной вкладки
    private MailingTabType getCurrentRole() {
        var selectedTab = tabSheet.getSelectedIndex();
        if (selectedTab > -1) {
            return rolesInOrder.get(selectedTab);
        }
        return MailingTabType.SENT;
    }

    // Определение типа диалога для создания рассылки на основе текущей роли
    private DialogType getCreateDialogType() {
        MailingTabType role = getCurrentRole();
        // Простой маппинг ролей табов в DialogType; при необходимости расширить
        return switch (role) {
            case TEMPLATES -> DialogType.TEMPLATES_CREATED;
            case DRAFT -> DialogType.DRAFT_CREATED;
            case SENT -> DialogType.SENT_CREATED;
        };
    }

    // Получение текущего действия CRUD на основе выбранной роли
    private CRUDActions<?> getCurrentAction() {
        return actions.get(getCurrentRole());
    }
}
