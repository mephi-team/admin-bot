package team.mephi.adminbot.vaadin.users.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.theme.lumo.LumoUtility;
import team.mephi.adminbot.dto.SimpleDirection;
import team.mephi.adminbot.dto.SimpleTutor;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.vaadin.users.components.PdPopover;

import java.util.Collection;

/**
 * Класс, содержащий различные рендереры для отображения информации о пользователях в таблицах.
 */
public class MyRenderers {
    /**
     * Создает рендерер для отображения информации о согласиях на обработку персональных данных пользователя.
     *
     * @return ComponentRenderer для отображения информации о согласиях на обработку персональных данных.
     */
    public static ComponentRenderer<Span, SimpleUser> createPdRenderer() {
        return new ComponentRenderer<>(user -> {
            Span span = new Span();
            var allPdCount = user.getPdConsentLog().size();
            var grantedPdCount = user.getPdConsentLog().stream().filter(pdLog -> pdLog.getStatus().equals("GRANTED")).count();
            if (allPdCount > 0) {
                Div content = new Div();
                content.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN);
                content.add(new H4(span.getTranslation("popover_pd_title")));
                for (var pdLog : user.getPdConsentLog()) {
                    content.add(new Div(new Span(pdLog.getSource()), new Span(" — "), new Span(span.getTranslation("pd_status_" + pdLog.getStatus().toLowerCase()))));
                }
                PdPopover popover = new PdPopover(content);
                popover.setTarget(span);
            }
            span.setText(span.getTranslation("popover_pd_text") + " " + grantedPdCount + "/" + allPdCount);
            span.addClassNames(LumoUtility.TextColor.PRIMARY);
            span.getElement().getStyle().set("text-decoration", "underline");
            return span;
        });
    }

    /**
     * Создает рендерер для отображения статуса пользователя.
     *
     * @return ComponentRenderer для отображения статуса пользователя.
     */
    public static ComponentRenderer<Span, SimpleUser> createStatusRenderer() {
        return new ComponentRenderer<>(user -> {
            Span span = new Span();
            String theme = switch (user.getStatus()) {
                case "ACTIVE" -> String.format("badge %s", "success");
                case "INACTIVE" -> String.format("badge %s", "contrast");
                case "BLOCKED" -> String.format("badge %s", "warning");
                case "PENDING" -> "badge";
                default -> String.format("badge %s", "error");
            };
            span.getElement().setAttribute("theme", theme);
            span.setText(span.getTranslation("user_status_" + user.getStatus().toLowerCase() + "_label"));
            return span;
        });
    }

    /**
     * Создает рендерер для отображения информации о кураторстве пользователя.
     *
     * @return ComponentRenderer для отображения информации о кураторстве.
     */
    public static ComponentRenderer<Span, SimpleTutor> createCuratorshipRenderer() {
        return new ComponentRenderer<>(user -> {
            Span span = new Span();
            var studentCount = user.getStudentCount();
            if (studentCount > 0) {
                Div content = new Div();
                content.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN);
                content.add(new H4(span.getTranslation("popover_curatorship_title")));
                for (var student : user.getStudents()) {
                    var fullName = new Span(student.getFullName());
                    fullName.addClassNames(LumoUtility.TextColor.PRIMARY);
                    fullName.getElement().getStyle().set("text-decoration", "underline");
                    content.add(new Div(fullName, new Span(", "), new Span(student.getTgId())));
                }
                PdPopover popover = new PdPopover(content);
                popover.setTarget(span);
            }
            span.setText(studentCount > 0 ? String.format("%d", studentCount) : "-");
            span.addClassNames(LumoUtility.TextColor.PRIMARY);
            return span;
        });
    }

    /**
     * Создает рендерер для отображения направлений пользователя.
     *
     * @return ComponentRenderer для отображения направлений пользователя.
     */
    public static ComponentRenderer<Div, SimpleUser> createUserDirections() {
        return new ComponentRenderer<>(user -> renderList(user.getDirection()));
    }

    /**
     * Создает рендерер для отображения направлений куратора.
     *
     * @return ComponentRenderer для отображения направлений куратора.
     */
    public static ComponentRenderer<Div, SimpleTutor> createTutorDirections() {
        return new ComponentRenderer<>(user -> renderList(user.getDirections()));
    }

    /**
     * Вспомогательный метод для рендеринга списка направлений.
     *
     * @param directions коллекция направлений.
     * @return Div, содержащий отрендеренные направления.
     */
    private static Div renderList(Collection<SimpleDirection> directions) {
        var res = new Div();
        res.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.Gap.XSMALL);
        directions.forEach(s -> {
            Span span = new Span(s.getName());
            span.addClassNames(LumoUtility.Overflow.HIDDEN, LumoUtility.TextOverflow.ELLIPSIS, LumoUtility.FontSize.SMALL);
            res.add(span);
        });
        return res;
    }
}
