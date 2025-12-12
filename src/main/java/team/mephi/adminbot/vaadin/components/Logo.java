package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;

@Tag("a")
public class Logo extends Component {
    public Logo(String text) {
        getElement().setText(text);
        getElement().getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("left", "var(--lumo-space-l)").set("margin", "0")
                .set("position", "absolute")
                .set("color", "black");
        getElement().setAttribute("href", "/v2");
    }
}
