package team.mephi.adminbot.vaadin.service;

import com.vaadin.flow.component.icon.Icon;
import team.mephi.adminbot.vaadin.SimpleDialog;
import team.mephi.adminbot.vaadin.components.SimpleConfirmDialog;

public interface DialogFactory {
    SimpleDialog getDialog(DialogType type);
    SimpleConfirmDialog getConfirmDialog(DialogType type, Icon icon);
}
