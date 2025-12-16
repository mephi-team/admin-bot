package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.function.SerializableRunnable;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.data.domain.Persistable;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.repository.TutorRepository;
import team.mephi.adminbot.repository.UserRepository;
import team.mephi.adminbot.vaadin.components.UserDrawer;
import team.mephi.adminbot.vaadin.components.UserCountBadge;
import team.mephi.adminbot.vaadin.components.UserDeleteDialog;
import team.mephi.adminbot.vaadin.providers.ProviderGet;
import team.mephi.adminbot.vaadin.views.users.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Route("/users")
@RolesAllowed("ADMIN")
public class Users extends VerticalLayout {
    UserDrawer driver;
    UserDeleteDialog dialog;
    Long deleteId;
    Map<String, Long> roleCounts;
    List<Component> tables;
    TabSheet tabSheet;

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

        tabSheet = new TabSheet();
        tabSheet.setSizeFull();

        roleCounts = userRepository.countsByRole();

        List<String> tabs = List.of("visitor", "candidate", "middle_candidate", "student", "free_listener", "lc_expert", "tutor");
        List<String> tabNames = List.of("Гости", "Кандидаты", "Миддл-Кандидаты", "Студенты", "Слушатели", "Эксперты", "Кураторы");

        Map<String, UserCountBadge> badges = tabs.stream().collect(Collectors.toMap(
                s -> s, key -> new UserCountBadge(roleCounts.getOrDefault(key, 0L))));

        tables = List.of(
                new GuestsView(userRepository, "visitor", onEdit(), onDelete()),
                new CandidateView(userRepository, "candidate", onEdit(), onDelete()),
                new MiddleCandidateView(userRepository, "middle_candidate", onEdit(), onDelete()),
                new StudentView(userRepository, "student", onEdit(), onDelete()),
                new FreeListenerView(userRepository, "free_listener", onEdit(), onDelete()),
                new ExpertsView(userRepository, "lc_expert", onEdit(), onDelete()),
                new TutorsView(tutorRepository, onEdit(), onDelete())
        );

        tabs.forEach(tab -> {
            int index = tabs.indexOf(tab);
            tabSheet.add(new Span(new Span(tabNames.get(index)), badges.get(tab)), tables.get(index));
        });

        driver = new UserDrawer((u) -> {
            System.out.println("!!!! User " + u);
            return u;
        }, onClose());

//        tabSheet.getSelectedIndex();
        dialog = new UserDeleteDialog(event -> {
            if(deleteId != null) {
                int tabIndex = tabSheet.getSelectedIndex();
                String tabCode = tabs.get(tabIndex);

                System.out.println("!!!! TAB INDEX " + tabSheet.getSelectedIndex());
                ProviderGet provider = (ProviderGet) tables.get(tabIndex);

                provider.getRepository().deleteById(deleteId);
                roleCounts = userRepository.countsByRole();

                badges.get(tabCode).setCount(roleCounts.getOrDefault(tabCode, 0L));
                provider.getProvider().refreshAll();
            }
            dialog.close();
            deleteId = null;
        });

        add(top, tabSheet, driver, dialog);
    }

    private Consumer<Persistable<Long>> onDelete() {
        return (s) -> {
            deleteId = s.getId();
            dialog.open();
        };
    }

    private Consumer<Persistable<Long>> onEdit() {
        return (s) -> {
            int tabIndex = tabSheet.getSelectedIndex();
            ProviderGet provider = (ProviderGet) tables.get(tabIndex);
            Optional<SimpleUser> e = ((UserRepository)provider.getRepository()).findSimpleUserById(s.getId());
            driver.setProposal(e.get());
        };
    }

    SerializableRunnable onClose() {
        return () -> {
            driver.setProposal(null);
        };
    }

    private Span createBadge(Long value) {
        Span badge = new Span(String.valueOf(value));
        badge.getElement().getThemeList().add("badge small contrast");
        badge.getStyle().set("margin-inline-start", "var(--lumo-space-xs)");
        return badge;
    }

}
