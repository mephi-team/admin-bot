package team.mephi.adminbot.vaadin.mailings.views;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import lombok.Getter;

@Getter
public enum Statuses {
    DRAFT(VaadinIcon.EDIT),
    ACTIVE(VaadinIcon.PLAY),
    PAUSED(VaadinIcon.PAUSE),
    FINISHED(VaadinIcon.CHECK_CIRCLE),
    ERROR(VaadinIcon.WARNING),
    CANCELED(VaadinIcon.CLOSE_CIRCLE);

    private final VaadinIcon icon;

    Statuses(VaadinIcon icon) {
        this.icon = icon;
    }

    public Icon createIcon() {
        return icon.create();
    }
}
