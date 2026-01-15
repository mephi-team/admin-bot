package team.mephi.adminbot.vaadin.dialogs.dataproviders;

import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import lombok.Getter;
import lombok.Setter;
import team.mephi.adminbot.dto.DialogWithLastMessageDto;
import team.mephi.adminbot.repository.DialogRepository;

import java.util.Optional;

public class DialogListDataProvider {

    private final DialogRepository dialogRepository;
    @Getter
    @Setter
    private Long currentUserId;
    private ConfigurableFilterDataProvider<DialogWithLastMessageDto, Void, String> provider;

    public DialogListDataProvider(DialogRepository dialogRepository) {
        this.dialogRepository = dialogRepository;
    }

    public ConfigurableFilterDataProvider<DialogWithLastMessageDto, Void, String> getFilterableProvider() {
        if (provider == null) {
            provider = new CallbackDataProvider<DialogWithLastMessageDto, String>(
                    query -> dialogRepository.findDialogsWithLastMessageNative(query.getFilter().orElse(""), Optional.ofNullable(getCurrentUserId()))
                            .stream()
                            .skip(query.getOffset())
                            .limit(query.getLimit()),
                    query -> dialogRepository.countDialogsWithLastMessageNative(query.getFilter().orElse(""), Optional.ofNullable(getCurrentUserId())),
                    DialogWithLastMessageDto::getDialogId
            ).withConfigurableFilter();
        }

        return provider;
    }
}
