package team.mephi.adminbot.vaadin.dialogs.dataproviders;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.repository.DialogRepository;

/**
 * Фабрика для создания экземпляров DialogListDataProvider.
 */
@SpringComponent
public class DialogListDataProviderFactory {

    private final DialogRepository dialogRepository;

    public DialogListDataProviderFactory(DialogRepository dialogRepository) {
        this.dialogRepository = dialogRepository;
    }

    public DialogListDataProvider createDataProvider() {
        return new DialogListDataProvider(dialogRepository);
    }
}
