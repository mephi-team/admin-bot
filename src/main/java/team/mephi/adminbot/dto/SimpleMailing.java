package team.mephi.adminbot.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleMailing {
    private Long id;
//    @NotEmpty
    private String name;
    @NotEmpty(message = "Текст обязателен")
    private String text;
    private String status;
    private Long userId;
    @NotEmpty(message = "Канал обязателен")
    private Set<String> channels;
    @NotEmpty(message = "Пользователи обязательны")
    private String users;
    @NotEmpty(message = "Город обязателен")
    private String cohort;
    @NotEmpty(message = "Направление обязательно")
    private String direction;
    @NotEmpty(message = "Город обязателен")
    private String city;
}
