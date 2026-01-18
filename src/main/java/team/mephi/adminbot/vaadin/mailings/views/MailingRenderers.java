package team.mephi.adminbot.vaadin.mailings.views;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.theme.lumo.LumoUtility;
import team.mephi.adminbot.dto.SimpleMailing;

public class MailingRenderers {
    public static ComponentRenderer<Span, SimpleMailing> createStatusRenderer() {
        return new ComponentRenderer<>(mailing -> {
            Span span = new Span();
            String theme = switch (mailing.getStatus()) {
                case "ACTIVE" -> "status badge";
                case "DRAFT" -> String.format("status badge %s", "contrast");
                case "PAUSED" -> String.format("status badge %s", "warning");
                case "FINISHED" -> String.format("status badge %s", "success");
                default -> String.format("status badge %s", "error");
            };
            span.getElement().setAttribute("theme", theme);
            span.addClassNames(LumoUtility.Display.INLINE_FLEX, LumoUtility.Gap.SMALL);
            var icon = Statuses.valueOf(mailing.getStatus()).createIcon();
            icon.setSize("12px");
            span.add(icon, new Span(span.getTranslation("mailing_status_" + mailing.getStatus().toLowerCase() + "_label")));
            return span;
        });
    }
}
