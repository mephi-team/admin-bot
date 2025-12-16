package team.mephi.adminbot.model.enums;

/**
 * Тип поля, по которому обнаружен дубликат заявки.
 * <p>
 * Используется для классификации дубликатов:
 * - EMAIL: дубликат по email адресу
 * - PHONE: дубликат по номеру телефона
 * - FULL_NAME: дубликат по полному имени (first_name + last_name)
 */
public enum DuplicateFieldType {
    EMAIL,      // Дубликат по email
    PHONE,      // Дубликат по телефону
    FULL_NAME   // Дубликат по полному имени
}

