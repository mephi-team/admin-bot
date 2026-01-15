package team.mephi.adminbot.vaadin.dialogs.dataproviders;

import com.vaadin.flow.spring.annotation.SpringComponent;
import team.mephi.adminbot.repository.DialogRepository;

/**
 * Фабрика для создания экземпляров DialogListDataProvider.
 */
@SpringComponent
public class DialogListDataProviderFactory {

    private final DialogRepository dialogRepository;

    /**
     * Конструктор фабрики с внедрением зависимости DialogRepository.
     *
     * @param dialogRepository репозиторий для работы с диалогами
     */
    public DialogListDataProviderFactory(DialogRepository dialogRepository) {
        this.dialogRepository = dialogRepository;
    }

    /**
     * Создает новый экземпляр DialogListDataProvider.
     *
     * @return новый экземпляр DialogListDataProvider
     */
    public DialogListDataProvider createDataProvider() {
        return new DialogListDataProvider(dialogRepository);
    }
}
