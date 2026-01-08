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
            content.add(new H4("Согласия на обработку ПД"), new Div(new Span("Согласие при регистрации"), new Span(" — "), new Span("не получено")));
            PdPopover popover = new PdPopover(content);
            popover.setTarget(span);

            span.setText("Получено: 1/1");
            span.addClassNames(LumoUtility.TextColor.PRIMARY);
            span.getElement().getStyle().set("text-decoration", "underline");
            return span;
        });
    }
}
