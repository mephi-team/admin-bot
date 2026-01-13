package team.mephi.adminbot.vaadin.users.presenter;

import team.mephi.adminbot.dto.SimpleTutor;
import team.mephi.adminbot.vaadin.CRUDDataProvider;

public interface TutorDataProvider extends CRUDDataProvider<SimpleTutor> {
    void blockAllById(Iterable<Long> ids);
}