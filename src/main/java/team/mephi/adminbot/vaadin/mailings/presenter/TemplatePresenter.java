package team.mephi.adminbot.vaadin.mailings.presenter;

import team.mephi.adminbot.dto.SimpleTemplate;
import team.mephi.adminbot.vaadin.CRUDDataProvider;
import team.mephi.adminbot.vaadin.CRUDPresenter;
import team.mephi.adminbot.vaadin.CRUDViewCallback;
import team.mephi.adminbot.vaadin.service.NotificationService;

public class TemplatePresenter extends CRUDPresenter<SimpleTemplate>  {
    public TemplatePresenter(CRUDDataProvider<SimpleTemplate> dataProvider, CRUDViewCallback<SimpleTemplate> view, NotificationService notificationService) {
        super(dataProvider, view, notificationService);
    }
}
