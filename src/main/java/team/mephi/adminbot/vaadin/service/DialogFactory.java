package team.mephi.adminbot.vaadin.service;

import com.vaadin.flow.component.icon.Icon;
import team.mephi.adminbot.vaadin.DialogWithTitle;
import team.mephi.adminbot.vaadin.components.dialogs.SimpleConfirmDialog;

public interface DialogFactory {
    DialogWithTitle getDialog(DialogType type);

    SimpleConfirmDialog getConfirmDialog(DialogType type, Icon icon);
}
