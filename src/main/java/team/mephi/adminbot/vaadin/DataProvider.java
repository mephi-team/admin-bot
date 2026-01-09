package team.mephi.adminbot.vaadin;

public interface DataProvider <T> {
    CRUDDataProvider<T> getDataProvider();
}
