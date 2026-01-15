package team.mephi.adminbot.repository;

import jakarta.transaction.Transactional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.mephi.adminbot.model.Expert;

import java.util.List;

/**
 * Репозиторий для работы с сущностью Expert.
 */
@Repository
@SuppressWarnings("unused")
public interface ExpertRepository extends JpaRepository<Expert, Long> {
    /**
     * Находит всех активных экспертов по их роли и имени с пагинацией.
     *
     * @param role     Роль эксперта.
     * @param query    Поисковый запрос для имени.
     * @param pageable Параметры пагинации.
     * @return Список экспертов.
     */
    @Query("SELECT e FROM Expert e JOIN fetch e.role LEFT JOIN FETCH e.directions LEFT JOIN FETCH e.tutorAssignments ta LEFT JOIN FETCH ta.tutor LEFT JOIN FETCH e.pdConsentLogs WHERE e.isActive AND e.role.code = :role AND (" +
            "LOWER(COALESCE(e.userName, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(e.name, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(e.firstName, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(e.lastName, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(e.externalId, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(e.tgId, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(e.email, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(e.phoneNumber, '')) LIKE LOWER(CONCAT('%', :query, '%'))" +
            ")")
    List<Expert> findAllByRoleAndName(String role, String query, Pageable pageable);

    /**
     * Подсчитывает количество активных экспертов по их роли и имени.
     *
     * @param role  Роль эксперта.
     * @param query Поисковый запрос для имени.
     * @return Количество экспертов.
     */
    @Query("SELECT count(e) FROM Expert e JOIN e.role WHERE e.isActive AND e.role.code = :role AND (" +
            "LOWER(COALESCE(e.userName, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(e.name, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(e.firstName, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(e.lastName, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(e.externalId, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(e.tgId, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(e.email, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(COALESCE(e.phoneNumber, '')) LIKE LOWER(CONCAT('%', :query, '%'))" +
            ")")
    Integer countByRoleAndName(String role, String query);

    /**
     * Логически удаляет экспертов по их идентификаторам, переключая их статус активности.
     *
     * @param ids Идентификаторы экспертов.
     */
    @Query("update Expert e set e.isActive = FUNCTION('NOT', e.isActive) WHERE e.id IN :ids")
    @Transactional
    @Modifying
    void deleteAllById(@Param("ids") @NonNull Iterable<? extends Long> ids);
}

