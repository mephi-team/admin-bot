package team.mephi.adminbot.vaadin.mailings.service;

import team.mephi.adminbot.vaadin.mailings.actions.MailingActions;
import team.mephi.adminbot.vaadin.mailings.dataproviders.MailingDataProvider;

import java.util.List;

public class MailingsPresenter  implements MailingActions {

    public MailingsPresenter(MailingDataProvider provider, MailingViewCallback view) {

    }

    @Override
    public void onCreate(String role) {

    }

    @Override
    public void onView(Long id) {

    }

    @Override
    public void onEdit(Long id) {

    }

    @Override
    public void onDelete(List<Long> ids) {

    }

    @Override
    public void onAccept(List<Long> ids) {

    }

    @Override
    public void onReject(List<Long> ids) {

    }
}
