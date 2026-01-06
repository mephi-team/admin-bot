package team.mephi.adminbot.vaadin.mailings.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.mephi.adminbot.dto.SimpleTemplate;
import team.mephi.adminbot.repository.MailTemplateRepository;
import team.mephi.adminbot.vaadin.mailings.components.TemplateService;

import java.util.List;

@Service
public class TemplateServiceImpl implements TemplateService {

    private final MailTemplateRepository mailTemplateRepository;

    public TemplateServiceImpl(MailTemplateRepository mailTemplateRepository) {
        this.mailTemplateRepository = mailTemplateRepository;
    }

    @Override
    public List<SimpleTemplate> findAll() {
        return mailTemplateRepository.findAll().stream().map(t -> SimpleTemplate.builder()
                .id(t.getId())
                .name(t.getName())
                .text(t.getBodyText())
                .build()
        ).toList();
    }

    @Override
    public List<SimpleTemplate> findAll(Pageable pageable, String s) {
        return mailTemplateRepository.findAllByName(s, pageable).stream().map(t -> SimpleTemplate.builder()
                .id(t.getId())
                .name(t.getName())
                .text(t.getBodyText())
                .build())
                .toList();
    }
}
