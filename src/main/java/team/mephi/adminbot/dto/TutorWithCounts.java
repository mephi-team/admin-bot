package team.mephi.adminbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Persistable;

@Data
@AllArgsConstructor
public class TutorWithCounts implements Persistable<Long> {
    private Long id;
    private String fullName;
    private String tgId;
    private String email;
    private Boolean delete;
    private Long studentCount;
    private String directions;

    @Override
    public boolean isNew() {
        return false;
    }
}
