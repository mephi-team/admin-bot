package team.mephi.adminbot.dto;

import lombok.Builder;
import lombok.Data;

import java.nio.file.Path;

@Data
@Builder
public class SimpleFile {
    private String name;
    private String type;
    private Long size;
    private Path content;
}
