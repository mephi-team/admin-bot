package team.mephi.adminbot.vaadin.users.views;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import lombok.Getter;

@Getter
public enum Statuses {
    ACTIVE(VaadinIcon.CHECK),
    EXPELLED(VaadinIcon.TRASH),
    INACTIVE(VaadinIcon.CLOSE_CIRCLE),
    BLOCKED(VaadinIcon.BAN),
    PENDING(VaadinIcon.HOURGLASS);

    private final VaadinIcon icon;

    Statuses(VaadinIcon icon) {
        this.icon = icon;
    }

    public Icon createIcon() {
        return icon.create();
    }
}
