package team.mephi.adminbot.vaadin.components;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * Компонент для отображения количества пользователей в виде бейджа.
 */
public class UserCountBadge extends Span {
    /**
     * Создает бейдж с указанным количеством пользователей.
     *
     * @param value количество пользователей
     */
    public UserCountBadge(Long value) {
        setText(String.format("(%d)", value));
        addClassNames(LumoUtility.Margin.Left.XSMALL);
    }

    /**
     * Устанавливает количество пользователей и обновляет отображение.
     *
     * @param value количество пользователей
     */
    public void setCount(Long value) {
        setText(String.format("(%d)", value));
    }
}
