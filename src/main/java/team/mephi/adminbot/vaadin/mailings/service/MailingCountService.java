package team.mephi.adminbot.vaadin.mailings.service;

import org.springframework.stereotype.Service;
import team.mephi.adminbot.repository.MailingRepository;

import java.util.Map;

@Service
public class MailingCountService {
    private final MailingRepository mailingRepository;

    public MailingCountService(MailingRepository mailingRepository) {
        this.mailingRepository = mailingRepository;
    }

    public Map<String, Long> getAllCounts() {
        return mailingRepository.countsByStatus();
    }
}
