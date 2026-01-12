package team.mephi.adminbot.service;

import team.mephi.adminbot.dto.ChatListItem;
import team.mephi.adminbot.dto.SimpleDialog;

import java.util.List;
import java.util.Optional;

public interface MessageService {
    List<ChatListItem> findAllByDialogId(Long dialogId);
    Optional<SimpleDialog> findById(Long dialogId);
    Integer countByDialogId(Long dialogId);
    void send(Long dialogId, String messageText);
}
