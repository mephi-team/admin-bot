package team.mephi.adminbot.vaadin.service;

import team.mephi.adminbot.vaadin.SimpleDialog;
import team.mephi.adminbot.vaadin.components.SimpleConfirmDialog;

public interface DialogFactory {
    SimpleDialog getDialog(String name);
    SimpleConfirmDialog getConfirmDialog(String name);
}
