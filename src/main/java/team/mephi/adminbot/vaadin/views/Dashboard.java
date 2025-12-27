package team.mephi.adminbot.vaadin.views;

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
import team.mephi.adminbot.repository.DialogRepository;
import team.mephi.adminbot.repository.UserQuestionRepository;

@Route("/")
@PermitAll
public class Dashboard extends VerticalLayout {
    public Dashboard(DialogRepository dialogRepository, UserQuestionRepository userQuestionRepository) {
        setAlignItems(Alignment.CENTER);
        add(new H1(getTranslation("page_dashboard_title")));

        HorizontalLayout layout = new HorizontalLayout();
        layout.setPadding(false);
        layout.setMargin(false);
        layout.setMaxWidth("970px");
        layout.setPadding(true);
        layout.setWrap(true);
        layout.setJustifyContentMode(JustifyContentMode.START);

        Card card1 = new Card();
        card1.setMinHeight("136px");
        card1.addThemeVariants(CardVariant.LUMO_ELEVATED);
        card1.setHeaderPrefix(new Icon(VaadinIcon.ENVELOPE_O));
        card1.setWidth("300px");
        card1.setTitle(new Div(getTranslation("page_dashboard_mailing_card_title")));
        card1.addToFooter(new RouterLink(getTranslation("go_button"), Mailings.class));

        Card card2 = new Card();
        card2.setMinHeight("136px");
        card2.addThemeVariants(CardVariant.LUMO_ELEVATED);
        card2.setHeaderPrefix(new Icon(VaadinIcon.CHAT));
        card2.setWidth("300px");
        card2.setTitle(new Div(getTranslation("page_dashboard_dialogs_card_title")));
        Span newDialogs = new Span(dialogRepository.unreadCount().toString());
        newDialogs.getElement().getThemeList().add("badge success");
        card2.add(new Span(newDialogs, new Span(" "), new Span(getTranslation("page_dashboard_dialogs_card_new_messages"))));
        card2.addToFooter(new RouterLink(getTranslation("go_button"), Dialogs.class));

        Card card3 = new Card();
        card3.setMinHeight("136px");
        card3.addThemeVariants(CardVariant.LUMO_ELEVATED);
        card3.setHeaderPrefix(new Icon(VaadinIcon.QUESTION));
        card3.setWidth("300px");
        card3.setTitle(new Div(getTranslation("page_dashboard_questions_card_title")));
        Span newQuestions = new Span(userQuestionRepository.countNewQuestion().toString());
        newQuestions.getElement().getThemeList().add("badge success");
        card3.add(new Span(newQuestions, new Span(" "), new Span(getTranslation("page_dashboard_questions_card_new_questions"))));
        card3.addToFooter(new RouterLink(getTranslation("go_button"), Questions.class));

        Card card4 = new Card();
        card4.setMinHeight("136px");
        card4.addThemeVariants(CardVariant.LUMO_ELEVATED);
        card4.setHeaderPrefix(new Icon(VaadinIcon.USERS));
        card4.setWidth("300px");
        card4.setTitle(new Div(getTranslation("page_dashboard_users_card_title")));
        card4.addToFooter(new RouterLink(getTranslation("go_button"), Users.class));

        Card card5 = new Card();
        card5.setMinHeight("136px");
        card5.addThemeVariants(CardVariant.LUMO_ELEVATED);
        card5.setHeaderPrefix(new Icon(VaadinIcon.BAR_CHART));
        card5.setWidth("300px");
        card5.setTitle(new Div(getTranslation("page_dashboard_analytics_card_title")));
        card5.addToFooter(new RouterLink(getTranslation("go_button"), Analytics.class));

        layout.add(card1, card2, card3, card4, card5);

        add(layout);
    }
}
