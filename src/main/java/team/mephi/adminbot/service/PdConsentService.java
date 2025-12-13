package team.mephi.adminbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.mephi.adminbot.dto.PdConsentUiDto;
import team.mephi.adminbot.model.PdConsentLog;
import team.mephi.adminbot.model.enums.ConsentStatus;
import team.mephi.adminbot.repository.PdConsentLogRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PdConsentService {
    private final PdConsentLogRepository repository;

    // UI-view (НЕ entity)
    public record PdConsentView(
            int received,
            int total,
            String tooltip  // текст для popup
    ) {}

    // метод, который вызывает UserController
    public PdConsentView buildForUserView(Long userId) {

        List<PdConsentLog> logs = repository.findLatestByUserId(userId);

        if (logs.isEmpty()) {
            return new PdConsentView(
                    0,
                    1,
                    "Согласие при регистрации — не получено"
            );
        }

        PdConsentLog last = logs.get(0);

        boolean granted = last.getStatus() == ConsentStatus.GRANTED;

        String tooltip = granted
                ? "Согласие при регистрации — получено"
                : "Согласие при регистрации — отозвано";

        return new PdConsentView(
                granted ? 1 : 0,
                1,
                tooltip
        );
    }
}
