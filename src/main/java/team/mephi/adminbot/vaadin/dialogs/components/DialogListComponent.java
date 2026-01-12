package team.mephi.adminbot.vaadin.dialogs.components;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;
import team.mephi.adminbot.dto.DialogWithLastMessageDto;
import team.mephi.adminbot.vaadin.components.SearchField;
import team.mephi.adminbot.vaadin.dialogs.dataproviders.DialogListDataProvider;
import team.mephi.adminbot.vaadin.dialogs.dataproviders.DialogListDataProviderFactory;
import team.mephi.adminbot.vaadin.views.Dialogs;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AnonymousAllowed
public class DialogListComponent extends VerticalLayout implements AfterNavigationObserver, BeforeEnterObserver {
    private final Instant today;
    protected DialogListDataProvider provider;
    private Long activeDialogId;

    ComponentRenderer<RouterLink, DialogWithLastMessageDto> cardRenderer = new ComponentRenderer<>(item -> {
        RouterLink link = new RouterLink();
        link.setHighlightCondition((a, e) -> false);
        link.addClassNames(LumoUtility.TextColor.BODY, LumoUtility.Overflow.HIDDEN);
        link.getElement().getStyle().set("text-decoration", "none");

        link.setRoute(Dialogs.class, new RouteParameters("dialogId", item.getDialogId().toString()));
        if (provider.getCurrentUserId() != null) {
            link.setQueryParameters(new QueryParameters(Map.of("userId", List.of("" + provider.getCurrentUserId()))));
        }

        Div content = new Div();
        content.addClassNames(LumoUtility.Display.FLEX, LumoUtility.Padding.MEDIUM, LumoUtility.BorderRadius.LARGE);

        if (item.getDialogId().equals(activeDialogId)) {
            content.addClassNames(LumoUtility.Background.PRIMARY_10);
        }

        Div mainContent = new Div();
        mainContent.addClassNames(LumoUtility.Overflow.HIDDEN, LumoUtility.Flex.AUTO);
        content.add(mainContent);

        // Заголовок: ФИО + дата
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setAlignItems(FlexComponent.Alignment.START);

        Span fullName = new Span();
        fullName.addClassNames(LumoUtility.FontWeight.BOLD, LumoUtility.Whitespace.NOWRAP, LumoUtility.Overflow.HIDDEN, LumoUtility.TextOverflow.ELLIPSIS);
        fullName.setText(item.getUserLastName() + " " + item.getUserFirstName());
        header.add(fullName);

        Span right = new Span();
        right.addClassNames(LumoUtility.Whitespace.NOWRAP);
        Span date = formatDate(item.getLastMessageAt());
        date.addClassNames(LumoUtility.FontSize.XSMALL, LumoUtility.Whitespace.NOWRAP, LumoUtility.TextColor.SECONDARY);
        right.add(date);
        if (item.getUnreadCount() > 0) {
            Span counter = new Span("" + item.getUnreadCount());
            counter.getElement().getThemeList().add("badge success");
            right.add(new Span(" "), counter);
        }
        header.add(right);

        mainContent.add(header);

        // Роль и externalId
        Span roleInfo = new Span();
        roleInfo.addClassNames(LumoUtility.Whitespace.NOWRAP, LumoUtility.FontSize.XSMALL, LumoUtility.TextColor.SECONDARY);
        roleInfo.setText(
                item.getUserRoleDescription().toLowerCase() +
                        " | " + item.getUserExternalId()
        );
        mainContent.add(roleInfo);

        // Последнее сообщение
        Span lastMessage = new Span();
        lastMessage.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.Display.BLOCK, LumoUtility.Whitespace.NOWRAP, LumoUtility.Overflow.HIDDEN, LumoUtility.TextOverflow.ELLIPSIS);

        Span senderName = new Span();
        senderName.addClassName(LumoUtility.TextColor.PRIMARY);
        senderName.setText(item.getLastMessageSenderName());

        Span messageText = new Span();
        messageText.setText(item.getLastMessageText());

        lastMessage.add(senderName, new Text(" "), messageText);
        mainContent.add(lastMessage);

        link.add(content);
        return link;
    });

    public DialogListComponent(DialogListDataProviderFactory dataProviderFactory) {
        this.today = Instant.now();

        setHeightFull();
        setPadding(false);

        final TextField searchField = new SearchField(getTranslation("page_dialogs_search_label"));
        searchField.setWidth("100%");

        provider = dataProviderFactory.createDataProvider();

        VirtualList<DialogWithLastMessageDto> list = new VirtualList<>();
        list.setDataProvider(provider.getFilterableProvider());
        list.setRenderer(cardRenderer);

        searchField.addValueChangeListener(e -> {
            provider.getFilterableProvider().setFilter(e.getValue());
        });

        add(searchField, list);
    }

    private Span formatDate(Instant dateTime) {
        Span date = new Span();
        if (dateTime == null) return date;

        DateTimeFormatter todayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String datePart = dateTime.atZone(ZoneOffset.UTC).format(todayFormatter);
        String todayPart = today.atZone(ZoneOffset.UTC).format(todayFormatter);

        if (datePart.equals(todayPart)) {
            date.getElement().executeJs("const f=new Intl.DateTimeFormat(navigator.language, {hour: 'numeric', minute: 'numeric'});this.innerHTML=f.format(new Date($0));", dateTime.toString());
        } else {
            date.getElement().executeJs("const f=new Intl.DateTimeFormat(navigator.language, {day: 'numeric', month: 'short'});this.innerHTML=f.format(new Date($0));", dateTime.toString());
        }
        return date;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        // Извлекаем ID из текущего URL (вам нужно адаптировать эту логику под ваши маршруты)
        Optional<String> dialogId = event.getRouteParameters().get("dialogId");
        if (dialogId.isPresent()) {
            try {
                this.activeDialogId = Long.parseLong(dialogId.get());
                // После получения ID, перерендериваем список, чтобы применились стили
                provider.getFilterableProvider().refreshAll();
            } catch (NumberFormatException e) {
                // Обработка ошибки, если ID не число
            }
        } else {
            this.activeDialogId = null;
            provider.getFilterableProvider().refreshAll();
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        provider.setCurrentUserId(event.getLocation().getQueryParameters().getSingleParameter("userId").map(Long::parseLong).orElse(null));
    }
}
