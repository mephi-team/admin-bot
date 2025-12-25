package team.mephi.adminbot.vaadin.mailings.service;

import team.mephi.adminbot.dto.SimpleMailing;
import team.mephi.adminbot.vaadin.CRUDPresenter;
import team.mephi.adminbot.vaadin.CRUDViewCallback;
import team.mephi.adminbot.vaadin.mailings.dataproviders.MailingDataProvider;


public class MailingsPresenter extends CRUDPresenter<SimpleMailing> {
    public MailingsPresenter(MailingDataProvider<SimpleMailing> dataProvider, CRUDViewCallback<SimpleMailing> view) {
        super(dataProvider, view);
    }
}
