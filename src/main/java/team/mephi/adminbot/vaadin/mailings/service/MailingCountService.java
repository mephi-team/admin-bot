package team.mephi.adminbot.vaadin.mailings.service;

import org.springframework.stereotype.Service;
import team.mephi.adminbot.repository.MailingRepository;

import java.util.Map;

/**
 * Сервис для получения количества рассылок по статусам.
 */
@Service
public class MailingCountService {
    private final MailingRepository mailingRepository;

    /**
     * Конструктор сервиса рассылок.
     *
     * @param mailingRepository репозиторий для работы с рассылками
     */
    public MailingCountService(MailingRepository mailingRepository) {
        this.mailingRepository = mailingRepository;
    }

    /**
     * Получает количество рассылок по каждому статусу.
     *
     * @return карта, где ключ - статус рассылки, значение - количество рассылок с этим статусом
     */
    public Map<String, Long> getAllCounts() {
        return mailingRepository.countsByStatus();
    }
}
