package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import jakarta.annotation.security.PermitAll;
import team.mephi.adminbot.service.AuthService;
import team.mephi.adminbot.vaadin.dashboard.dataproviders.DashboardDataProviderFactory;

/**
 * Представление главной панели управления.
 * Отображает различные карточки с навигацией к разделам приложения.
 * Доступно всем аутентифицированным пользователям.
 */
@Route("/")
@PermitAll
public class Dashboard extends VerticalLayout {
    public Dashboard(DashboardDataProviderFactory factory, AuthService authService) {
        var provider = factory.create();
        setAlignItems(Alignment.CENTER);
        add(new H1(getTranslation("page_dashboard_title")));

        Card card1 = createCard("page_dashboard_mailing_card_title", VaadinIcon.ENVELOPE_O.create(), Mailings.class);

        Card card2 = createCard("page_dashboard_dialogs_card_title", VaadinIcon.CHAT.create(), Dialogs.class);
        card2.add(createBadge(provider.unreadCount(), "page_dashboard_dialogs_card_new_messages", "page_dashboard_dialogs_card_empty_messages"));

        Card card3 = createCard("page_dashboard_questions_card_title", VaadinIcon.QUESTION.create(), Questions.class);
        card3.add(createBadge(provider.countNewQuestion(), "page_dashboard_questions_card_new_questions", "page_dashboard_questions_card_empty_questions"));

        Card card4 = createCard("page_dashboard_users_card_title", VaadinIcon.USERS.create(), Users.class);
        Card card5 = createCard("page_dashboard_analytics_card_title", VaadinIcon.BAR_CHART.create(), Analytics.class);

        if (authService.isAdmin())
            add(buildCardArea(card1, card2, card3, card4, card5));
        else
            add(buildCardArea(card3));
    }

    private HorizontalLayout buildCardArea(Component... components) {
        HorizontalLayout layout = new HorizontalLayout(components);
        layout.setPadding(false);
        layout.setMargin(false);
        layout.setMaxWidth("970px");
        layout.setPadding(true);
        layout.setWrap(true);
        layout.setJustifyContentMode(JustifyContentMode.START);
        return layout;
    }

    private Span createBadge(Integer count, String key, String empty) {
        Span newDialogs = new Span(count.toString());
        newDialogs.getElement().getThemeList().add("badge success");
        if (count > 0)
            return new Span(newDialogs, new Span(" "), new Span(getTranslation(key)));
        else
            return new Span(getTranslation(empty));
    }

    private Card createCard(String key, Icon icon, Class<? extends com.vaadin.flow.component.Component> navigationTarget) {
        Card card = new Card();
        card.setMinHeight("136px");
        card.addThemeVariants(CardVariant.LUMO_ELEVATED);
        card.setHeaderPrefix(icon);
        card.setWidth("300px");
        card.setTitle(new Div(getTranslation(key)));
        card.addToFooter(new RouterLink(getTranslation("go_button"), navigationTarget));
        return card;
    }
}
