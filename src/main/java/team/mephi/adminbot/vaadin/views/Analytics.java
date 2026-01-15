package team.mephi.adminbot.vaadin.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabSheetVariant;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import team.mephi.adminbot.vaadin.analytics.presenter.ChartPresenterFactory;
import team.mephi.adminbot.vaadin.analytics.tabs.AnalyticsTabProvider;

import java.util.Comparator;
import java.util.List;

/**
 * Представление страницы аналитики с вкладками для различных отчетов.
 * Доступно только пользователям с ролью ADMIN.
 */
@Route("/analytics")
@RolesAllowed("ADMIN")
public class Analytics extends VerticalLayout {
    /**
     * Конструктор для создания представления страницы аналитики.
     *
     * @param tabProviders     список провайдеров вкладок аналитики.
     * @param presenterFactory фабрика для создания презентеров диаграмм.
     */
    public Analytics(List<AnalyticsTabProvider> tabProviders, ChartPresenterFactory presenterFactory) {
        setSizeFull();
        getElement().getStyle().set("padding-inline", "120px");
        add(new H1(getTranslation("page_analytics_title")));

        TabSheet tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        tabSheet.addThemeVariants(TabSheetVariant.LUMO_TABS_MINIMAL);

        tabProviders.sort(Comparator.comparingInt(AnalyticsTabProvider::getPosition));

        for (var provider : tabProviders) {
            var content = provider.createTabContent(presenterFactory.createPresenter());

            Span tabContent = new Span(getTranslation(provider.getTabLabel()));
            var tab = new Tab(tabContent);
            tabSheet.add(tab, content, provider.getPosition());
        }

        add(tabSheet);
    }
}
