package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import team.mephi.adminbot.dto.TutorWithCounts;
import team.mephi.adminbot.model.Tutor;
import team.mephi.adminbot.model.User;
import team.mephi.adminbot.repository.TutorRepository;
import team.mephi.adminbot.repository.UserRepository;

import java.util.List;
import java.util.Map;

@Route("/users")
public class Users extends VerticalLayout {
    public Users(UserRepository userRepository, TutorRepository tutorRepository) {
        setHeightFull();
        HorizontalLayout top = new HorizontalLayout();
        top.setWidthFull();

        top.addToStart(new H1("Пользователи"));
        var primaryButton = new Button("Добавить пользователя", new Icon(VaadinIcon.PLUS));
        primaryButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Div buttons = new Div(new Button("Загрузить из файла", new Icon(VaadinIcon.FILE_ADD)), primaryButton);
        buttons.getElement().getStyle().set("display","flex");
        buttons.getElement().getStyle().set("gap","24px");
        top.addToEnd(buttons);

        TabSheet tabSheet = new TabSheet();

        tabSheet.setSizeFull();

        Map<String, Long> roleCounts = userRepository.countsByRole();

        tabSheet.add(new Span(new Span("Гости"), createBadge(roleCounts.getOrDefault("visitor", 0L))), roleCounts.getOrDefault("visitor", 0L) > 0 ? new UsersView(userRepository, "visitor") : new Span("Гостей пока нет"));
        tabSheet.add(new Span(new Span("Кандидаты"), createBadge(roleCounts.getOrDefault("candidate", 0L))), roleCounts.getOrDefault("candidate", 0L) > 0 ? new UsersView(userRepository, "candidate") : new Span("Кандидатов пока нет"));
        tabSheet.add(new Span(new Span("Миддл-Кандидаты"), createBadge(roleCounts.getOrDefault("middle_candidate", 0L))), roleCounts.getOrDefault("middle_candidate", 0L) > 0 ? new UsersView(userRepository, "middle_candidate") : new Span("Миддл-Кандидатов пока нет"));
        tabSheet.add(new Span(new Span("Студенты"), createBadge(roleCounts.getOrDefault("student", 0L))), roleCounts.getOrDefault("student", 0L) > 0 ? new UsersView(userRepository, "student") : new Span("Студентов пока нет"));
        tabSheet.add(new Span(new Span("Слушатели"), createBadge(roleCounts.getOrDefault("free_listener", 0L))), roleCounts.getOrDefault("free_listener", 0L) > 0 ? new UsersView(userRepository, "free_listener") : new Span("Слушателей пока нет"));
        tabSheet.add(new Span(new Span("Эксперты"), createBadge(roleCounts.getOrDefault("expert", 0L))), roleCounts.getOrDefault("expert", 0L) > 0 ? new UsersView(userRepository, "expert") : new Span("Экспертов пока нет"));
        tabSheet.add(new Span(new Span("Кураторы"), createBadge(roleCounts.getOrDefault("tutor", 0L))), new TutorsView(tutorRepository));

        add(top, tabSheet);
    }

    private Span createBadge(Long value) {
        Span badge = new Span(String.valueOf(value));
        badge.getElement().getThemeList().add("badge small contrast");
        badge.getStyle().set("margin-inline-start", "var(--lumo-space-xs)");
        return badge;
    }

}
