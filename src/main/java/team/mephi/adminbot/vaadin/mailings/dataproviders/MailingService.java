package team.mephi.adminbot.vaadin.mailings.dataproviders;

import team.mephi.adminbot.dto.SimpleMailing;

public interface MailingService {
    SimpleMailing save(SimpleMailing mailing);
    void deleteAllById(Iterable<Long> ids);
}
