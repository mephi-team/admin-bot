package team.mephi.adminbot.model.enums;

/**
 * Действия, которые могут быть зафиксированы в аудите системы.
 *
 * <p>Перечисление включает основные типы действий:
 * <ul>
 *   <li>CREATE - создание нового объекта</li>
 *   <li>UPDATE - обновление существующего объекта</li>
 *   <li>DELETE - удаление объекта</li>
 *   <li>VIEW - просмотр объекта</li>
 *   <li>EXPORT - экспорт данных</li>
 *   <li>IMPORT - импорт данных</li>
 * </ul>
 */
public enum AuditAction {
    CREATE,
    UPDATE,
    DELETE,
    VIEW,
    EXPORT,
    IMPORT
}

