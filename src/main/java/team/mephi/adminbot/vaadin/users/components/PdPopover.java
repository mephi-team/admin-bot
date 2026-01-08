package team.mephi.adminbot.vaadin.users.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.popover.PopoverPosition;

public class PdPopover extends Popover {
    public PdPopover(Component... components) {
        super(components);
        setPosition(PopoverPosition.BOTTOM_START);
        setOpenOnClick(false);
        setOpenOnHover(true);
        setOpenOnFocus(true);
    }
}
