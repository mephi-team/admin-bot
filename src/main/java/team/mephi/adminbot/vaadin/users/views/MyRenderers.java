package team.mephi.adminbot.vaadin.users.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.theme.lumo.LumoUtility;
import team.mephi.adminbot.dto.SimpleUser;
import team.mephi.adminbot.vaadin.users.components.PdPopover;

public class MyRenderers {
    public static ComponentRenderer<Span, SimpleUser> createPdRenderer() {
        return new ComponentRenderer<>(user -> {
            Span span = new Span();
            Div content = new Div();
            content.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN);
            content.add(new H4(span.getTranslation("popover_pd_title")), new Div(new Span(span.getTranslation("popover_pd_key_1")), new Span(" — "), new Span(user.getPdConsentLog().isEmpty() ? span.getTranslation("popover_pd_value_negative") : span.getTranslation("popover_pd_value_positive"))));
            PdPopover popover = new PdPopover(content);
            popover.setTarget(span);

            span.setText(span.getTranslation("popover_pd_text") + " " + user.getPdConsentLog().size() + "/1");
            span.addClassNames(LumoUtility.TextColor.PRIMARY);
            span.getElement().getStyle().set("text-decoration", "underline");
            return span;
        });
    }

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
}
