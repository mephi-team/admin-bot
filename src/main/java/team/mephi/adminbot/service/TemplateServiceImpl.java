package team.mephi.adminbot.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.mephi.adminbot.dto.SimpleTemplate;
import team.mephi.adminbot.model.MailTemplate;
import team.mephi.adminbot.repository.MailTemplateRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class TemplateServiceImpl implements TemplateService {

    private final MailTemplateRepository mailTemplateRepository;

    public TemplateServiceImpl(MailTemplateRepository mailTemplateRepository) {
        this.mailTemplateRepository = mailTemplateRepository;
    }

    @Override
    public List<SimpleTemplate> findAll() {
        return mailTemplateRepository.findAll()
                .stream()
                .map(this::mapToSimple)
                .toList();
    }

    @Override
    public List<SimpleTemplate> findAll(Pageable pageable, String s) {
        return mailTemplateRepository.findAllByName(s, pageable)
                .stream()
                .map(this::mapToSimple)
                .toList();
    }

    @Override
    @Transactional
    public SimpleTemplate save(SimpleTemplate template) {
        var result = template.getId() != null
                ? mailTemplateRepository.findById(template.getId()).orElse(new MailTemplate())
                : new MailTemplate();
        result.setName(template.getName());
        result.setSubject(template.getName());
        result.setBodyText(template.getText());
        mailTemplateRepository.save(result);
        return mapToSimple(result);
    }

    @Override
    public Optional<SimpleTemplate> findById(Long id) {
        return mailTemplateRepository.findById(id)
                .map(this::mapToSimple);
    }

    @Override
    public Stream<SimpleTemplate> findAllByName(String name, Pageable pageable) {
        return mailTemplateRepository.findAllByName(name, pageable)
                .stream()
                .map(this::mapToSimple);
    }

    @Override
    public Integer countByName(String name) {
        return mailTemplateRepository.countByName(name);
    }

    @Override
    public void deleteAllById(Iterable<Long> ids) {
        mailTemplateRepository.deleteAllById(ids);
    }

    private SimpleTemplate mapToSimple(MailTemplate template) {
        return SimpleTemplate.builder()
                .id(template.getId())
                .name(template.getName())
                .text(template.getBodyText())
                .build();
    }
}
