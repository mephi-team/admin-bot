package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import lombok.Getter;
import lombok.Setter;
import team.mephi.adminbot.dto.DialogWithLastMessageDto;
import team.mephi.adminbot.dto.MessagesForListDto;
import team.mephi.adminbot.model.Dialog;
import team.mephi.adminbot.model.Question;
import team.mephi.adminbot.repository.DialogRepository;
import team.mephi.adminbot.repository.MessageRepository;
import team.mephi.adminbot.repository.QuestionRepository;
import team.mephi.adminbot.vaadin.components.ChatListComponent;
import team.mephi.adminbot.vaadin.components.DialogListComponent;

@Route(value = "/dialogs/:dialogId?", layout = DialogsLayout.class)
public class Dailogs extends VerticalLayout implements HasUrlParameter<Long>, BeforeEnterObserver {
    private final VerticalLayout leftColumn = new VerticalLayout();
    private final VerticalLayout rightColumn = new VerticalLayout();
    private final DialogListComponent dialogList;
    private final ChatListComponent chatList;
    @Setter
    @Getter
    private Long dialogId;

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long parameter) {
        dialogId = parameter;
        if (parameter == null) {
//            dialogList.getElement().removeAttribute("selected");
        } else {
//            dialogList.getElement().setAttribute("selected", String.valueOf(parameter));
        }
    }

    public Dailogs(DialogRepository dialogRepository, MessageRepository messageRepository) {
//        getElement().getStyle().set("border", "3px solid blue");
        getElement().getStyle().set("height", "100%");
        getElement().getStyle().set("overflow", "hidden");

        setSizeFull();
//        setPadding(false);
//        setSpacing(false);
        addClassName("no-scroll-view");

        add(new H1("Диалоги"));

        leftColumn.setPadding(false);
        rightColumn.setPadding(false);
        HorizontalLayout contentLayout = new HorizontalLayout(leftColumn, rightColumn);
        contentLayout.setSizeFull();
        leftColumn.setWidth("30%");
//        leftColumn.getStyle().set("border", "1px solid red");
        rightColumn.setWidth("70%");
//        rightColumn.getStyle().set("border", "1px solid red");
        final TextField searchField = createSearchField();

        var filterableProvider = getProvider(dialogRepository, searchField);
        dialogList = new DialogListComponent(filterableProvider);
        leftColumn.add(searchField, dialogList);

        searchField.addValueChangeListener(e -> {
            // setFilter will refresh the data provider and trigger data
            // provider fetch / count queries. As a side effect, the pagination
            // controls will be updated.
            filterableProvider.setFilter(e.getValue());
        });

        CallbackDataProvider<MessagesForListDto, Long> dataProvider = new CallbackDataProvider<>(
                query -> {
                    // Используем Stream для получения нужного диапазона данных из репозитория
                    // В реальном приложении здесь обычно используется JpaSpecificationExecutor с пагинацией
                    return messageRepository.findAllByDialogId(getDialogId())
                            .stream().map(a -> new MessagesForListDto(a.getId(), a.getText(), a.getSenderType().name()))
                            .skip(query.getOffset()) // Пропускаем уже загруженные элементы
                            .limit(query.getLimit()); // Берем только нужное количество
                },
                // Метод count (подсчет общего количества результатов фильтрации)
                query -> messageRepository.countByDialogId(getDialogId())
        );

        chatList = new ChatListComponent(dataProvider);
        TextArea chat = new TextArea();
        chat.setMaxRows(4);
        chat.setMinRows(4);
        chat.setWidthFull();
        chat.getElement().getStyle().set("--vaadin-input-field-background","white");
        chat.getElement().getStyle().set("--vaadin-input-field-border-width","1px");
        chat.getElement().getStyle().set("--vaadin-input-field-border-color","#eaeaee");
        chat.getElement().getStyle().set("--vaadin-input-field-border-radius","12px");
//        chat.setMaxHeight("80px");
        Button button = new Button("Отправить");
        button.getElement().getStyle().set("align-self", "end");
        VerticalLayout v = new VerticalLayout();
        v.add(chatList);
        v.setHeightFull();
        v.getElement().getStyle().set("border", "1px solid #eaeaee");
        v.getElement().getStyle().set("border-radius", "12px");
        rightColumn.add(v, chat, button);

        add(contentLayout);
    }

    private TextField createSearchField() {
        TextField searchField = new TextField();
        searchField.setWidth("100%");
        searchField.setPlaceholder("Найти вопрос");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        return searchField;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Получаем ID из URL-параметра
        event.getRouteParameters().get("dialogId").ifPresent(dialogId -> {
            // Здесь вы можете загрузить данные диалога из сервиса по этому ID
            setDialogId(Long.valueOf(dialogId));
//            updateDetails(dialogId);
        });
    }

//    private void updateDetails(String id) {
//        // Имитация загрузки данных
////        rightColumn.getElement().setText("Загрузка деталей для диалога с ID: " + id);
//    }

    private static ConfigurableFilterDataProvider<DialogWithLastMessageDto, Void, String> getProvider(DialogRepository dialogRepository, TextField searchField) {
        CallbackDataProvider<DialogWithLastMessageDto, String> dataProvider = new CallbackDataProvider<>(
                query -> {
                    // Используем Stream для получения нужного диапазона данных из репозитория
                    // В реальном приложении здесь обычно используется JpaSpecificationExecutor с пагинацией
                    return dialogRepository.findDialogsWithLastMessageNative(searchField.getValue())
                            .stream()
                            .skip(query.getOffset()) // Пропускаем уже загруженные элементы
                            .limit(query.getLimit()); // Берем только нужное количество
                },
                // Метод count (подсчет общего количества результатов фильтрации)
                query -> dialogRepository.countDialogsWithLastMessageNative(searchField.getValue())
        );

        return dataProvider.withConfigurableFilter();
    }
}
