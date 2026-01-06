package team.mephi.adminbot.vaadin.mailings.components;

import org.springframework.data.domain.Pageable;
import team.mephi.adminbot.dto.SimpleTemplate;

import java.util.List;

public interface TemplateService {
    List<SimpleTemplate> findAll();
    List<SimpleTemplate> findAll(Pageable pageable, String s);
}
