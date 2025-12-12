package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import team.mephi.adminbot.dto.DialogWithLastMessageDto;
import team.mephi.adminbot.repository.DialogRepository;
import team.mephi.adminbot.repository.MessageRepository;
import team.mephi.adminbot.vaadin.components.ChatListComponent;
import team.mephi.adminbot.vaadin.components.DialogListComponent;

@Route(value = "/dialogs/:dialogId?", layout = DialogsLayout.class)
public class Dialogs extends VerticalLayout implements HasUrlParameter<Long> {
    private final VerticalLayout leftColumn = new VerticalLayout();
    private final VerticalLayout rightColumn = new VerticalLayout();
    private final DialogListComponent dialogList;
    private final ChatListComponent chatList;
    private final MessageInput chatInput;
    private final Div message = new Div("");

    public Dialogs(DialogRepository dialogRepository, MessageRepository messageRepository) {
        setSizeFull();

        leftColumn.setPadding(false);
        leftColumn.setWidth("30%");

        rightColumn.setPadding(false);
        rightColumn.setWidth("70%");

        HorizontalLayout contentLayout = new HorizontalLayout(leftColumn, rightColumn);
        contentLayout.setSizeFull();

        final TextField searchField = createSearchField();

        var filterableProvider = getProvider(dialogRepository, searchField);
        dialogList = new DialogListComponent(filterableProvider);
        leftColumn.add(searchField, dialogList);

        searchField.addValueChangeListener(e -> {
            filterableProvider.setFilter(e.getValue());
        });

        chatList = new ChatListComponent(messageRepository);
        chatInput = new MessageInput();
        chatInput.setWidthFull();

        rightColumn.add(chatList, chatInput);

        add(new H1("Диалоги"), contentLayout);
    }

    private TextField createSearchField() {
        TextField searchField = new TextField();
        searchField.setWidth("100%");
        searchField.setPlaceholder("Найти вопрос");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        return searchField;
    }

    private static ConfigurableFilterDataProvider<DialogWithLastMessageDto, Void, String> getProvider(DialogRepository dialogRepository, TextField searchField) {
        CallbackDataProvider<DialogWithLastMessageDto, String> dataProvider = new CallbackDataProvider<>(
                query -> {
                    return dialogRepository.findDialogsWithLastMessageNative(searchField.getValue())
                            .stream()
                            .skip(query.getOffset())
                            .limit(query.getLimit());
                },
                query -> dialogRepository.countDialogsWithLastMessageNative(searchField.getValue())
        );

        return dataProvider.withConfigurableFilter();
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long parameter) {
        if (parameter == null) {
            chatList.add(message);
        } else {
            chatList.add(chatList, chatInput);
        }
    }
}
