package team.mephi.adminbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Persistable;

@Data
@AllArgsConstructor
public class TutorWithCounts implements Persistable<Long> {
    private Long id;
    private String firstName;
    private String lastName;
    private String tgId;
    private String email;
    private Long studentCount;
    private String directions;

    @Override
    public boolean isNew() {
        return false;
    }
}
