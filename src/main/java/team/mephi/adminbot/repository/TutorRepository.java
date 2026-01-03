package team.mephi.adminbot.repository;

import jakarta.transaction.Transactional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.mephi.adminbot.model.Tutor;

import java.util.List;
import java.util.Optional;

public interface TutorRepository extends JpaRepository<Tutor, Long> {

    @Query("SELECT t FROM Tutor t LEFT JOIN FETCH t.studentAssignments LEFT JOIN FETCH t.directions WHERE LOWER(t.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(t.firstName) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Tutor> findAllWithDirectionsAndStudents(String query, Pageable pageable);

    @Query("SELECT count(t) FROM Tutor t WHERE LOWER(t.firstName) LIKE LOWER(CONCAT('%', :query, '%')) or LOWER(t.lastName) LIKE LOWER(CONCAT('%', :query, '%'))")
    Integer countByName(String query);

    @Query("update Tutor t set t.deleted = FUNCTION('NOT', t.deleted) WHERE t.id = :id")
    @Transactional
    @Modifying
    void deleteById(@NonNull Long id);

    @Query("update Tutor t set t.deleted = FUNCTION('NOT', t.deleted) WHERE t.id IN :ids")
    @Transactional
    @Modifying
    void deleteAllById(@Param("ids") Iterable<? extends Long> ids);

    @Query("update Tutor t set t.deleted = FUNCTION('NOT', t.deleted) WHERE t.id IN :ids")
    @Transactional
    @Modifying
    void blockAllById(@Param("ids") Iterable<? extends Long> ids);

    @Query("SELECT t FROM Tutor t WHERE t.id = :id")
    Optional<Tutor> findSimpleUserById(@NonNull Long id);
}
