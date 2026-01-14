package team.mephi.adminbot.dto;

import com.vaadin.flow.i18n.I18NProvider;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CohortDto {
    private String id;
    private String name;
    private Boolean current;
    public String getDisplayName() {
        return name + (current ? " ("+ I18NProvider.translate("current") + ")" : "");
    }
}
