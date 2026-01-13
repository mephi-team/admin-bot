package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabSheetVariant;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import team.mephi.adminbot.vaadin.CRUDActions;
import team.mephi.adminbot.vaadin.analytics.tabs.AnalyticsTabProvider;
import team.mephi.adminbot.vaadin.service.DialogType;

import java.util.Comparator;
import java.util.List;

@Route("/analytics")
@RolesAllowed("ADMIN")
public class Analytics extends VerticalLayout {
    public Analytics(List<AnalyticsTabProvider> tabProviders) {
        setSizeFull();
        getElement().getStyle().set("padding-inline", "120px");
        add(new H1(getTranslation("page_analytics_title")));

        TabSheet tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        tabSheet.addThemeVariants(TabSheetVariant.LUMO_TABS_MINIMAL);

        tabProviders.sort(Comparator.comparingInt(AnalyticsTabProvider::getPosition));

        for (var provider : tabProviders) {
            var content = provider.createTabContent(new CRUDActions<String>() {
                @Override public void onCreate(Object item, DialogType type, Object... param) {}
                @Override public void onView(String item, DialogType type) {}
                @Override public void onEdit(String item, DialogType type, Object... param) {}
                @Override public void onDelete(List<Long> ids, DialogType type, Object... param) {}
            });

            Span tabContent = new Span(getTranslation(provider.getTabLabel()));
            var tab = new Tab(tabContent);
            tabSheet.add(tab, content, provider.getPosition());
        }

        add(tabSheet);
    }
}
