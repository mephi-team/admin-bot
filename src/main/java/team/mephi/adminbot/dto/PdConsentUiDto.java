package team.mephi.adminbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PdConsentUiDto {
    private int received;
    private int total;
    private String tooltip; //текст для Popup
}
