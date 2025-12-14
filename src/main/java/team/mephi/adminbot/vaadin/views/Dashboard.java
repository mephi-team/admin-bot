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

@Route("/")
public class Dashboard extends VerticalLayout {
    public Dashboard() {
        setAlignItems(Alignment.CENTER);
        add(new H1("Управление чат-ботом УЦ"));

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
        card1.setTitle(new Div("Рассылки"));
        card1.addToFooter(new RouterLink("Перейти →", Mailings.class));

        Card card2 = new Card();
        card2.setMinHeight("136px");
        card2.addThemeVariants(CardVariant.LUMO_ELEVATED);
        card2.setHeaderPrefix(new Icon(VaadinIcon.CHAT));
        card2.setWidth("300px");
        card2.setTitle(new Div("Диалоги"));
        Span newDialogs = new Span("2");
        newDialogs.getElement().getThemeList().add("badge success");
        card2.add(new Span(newDialogs, new Span(" новых сообщения")));
        card2.addToFooter(new RouterLink("Перейти →", Dialogs.class));

        Card card3 = new Card();
        card3.setMinHeight("136px");
        card3.addThemeVariants(CardVariant.LUMO_ELEVATED);
        card3.setHeaderPrefix(new Icon(VaadinIcon.QUESTION));
        card3.setWidth("300px");
        card3.setTitle(new Div("Вопросы"));
        Span newQuestions = new Span("2");
        newQuestions.getElement().getThemeList().add("badge success");
        card3.add(new Span(newQuestions, new Span(" новых вопроса")));
        card3.addToFooter(new RouterLink("Перейти →", Questions.class));

        Card card4 = new Card();
        card4.setMinHeight("136px");
        card4.addThemeVariants(CardVariant.LUMO_ELEVATED);
        card4.setHeaderPrefix(new Icon(VaadinIcon.USERS));
        card4.setWidth("300px");
        card4.setTitle(new Div("Пользователи"));
        card4.addToFooter(new RouterLink("Перейти →", Users.class));

        Card card5 = new Card();
        card5.setMinHeight("136px");
        card5.addThemeVariants(CardVariant.LUMO_ELEVATED);
        card5.setHeaderPrefix(new Icon(VaadinIcon.BAR_CHART));
        card5.setWidth("300px");
        card5.setTitle(new Div("Аналитика"));
        card5.addToFooter(new RouterLink("Перейти →", Analytics.class));

        layout.add(card1, card2, card3, card4, card5);

        add(layout);
    }
}
