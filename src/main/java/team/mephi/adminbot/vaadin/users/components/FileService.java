package team.mephi.adminbot.vaadin.users.components;

import team.mephi.adminbot.dto.SimpleFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    void uploadAll(List<SimpleFile> files, String currentUser) throws IOException;
}
