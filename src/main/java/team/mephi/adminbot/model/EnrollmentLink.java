package team.mephi.adminbot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import team.mephi.adminbot.model.enums.EnrollmentStatus;

import java.time.LocalDateTime;

/**
 * Entity representing an enrollment link for out-of-flow user enrollment.
 * <p>
 * Each enrollment link is generated for a specific user within a batch and tracks:
 * <ul>
 *   <li><b>Lifecycle:</b> created → sent → used/expired/failed</li>
 *   <li><b>Delivery status:</b> whether the link has been sent to the user</li>
 *   <li><b>Expiration:</b> optional expiration timestamp</li>
 *   <li><b>Failure tracking:</b> status reason for failed or expired links</li>
 * </ul>
 * <p>
 * Relations:
 * <ul>
 *   <li>Each link belongs to exactly one {@link EnrollmentBatch}</li>
 *   <li>Each link is associated with exactly one {@link User}</li>
 * </ul>
 * <p>
 * Constraints:
 * <ul>
 *   <li>Link must be unique within the system</li>
 *   <li>created_at is immutable after insert</li>
 *   <li>expires_at must be after created_at (if present)</li>
 *   <li>is_sent defaults to false for newly created links</li>
 * </ul>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "enrollment_links")
public class EnrollmentLink {
    /**
     * Primary key identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * The enrollment batch this link belongs to.
     * Foreign key to enrollment_batches.batch_id.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id", nullable = false)
    private EnrollmentBatch batch;

    /**
     * The user this enrollment link is generated for.
     * Foreign key to users.id.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * The enrollment link URL.
     * Must be unique within the system.
     */
    @Column(name = "link", nullable = false)
    private String link;

    /**
     * Timestamp when the enrollment link was created.
     * Immutable after insert.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the enrollment link expires.
     * Nullable - if null, the link does not expire.
     * Must be after created_at if present.
     */
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    /**
     * Flag indicating whether the enrollment link has been sent to the user.
     * Defaults to false for newly created links.
     */
    @Column(name = "is_sent", nullable = false)
    @Builder.Default
    private boolean sent = false;

    /**
     * Current status of the enrollment link.
     * Reflects the lifecycle: PENDING, SENT, USED, EXPIRED, FAILED.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EnrollmentStatus status;

    /**
     * Reason for the current status (e.g., failure reason, expiration reason).
     * Nullable - only populated when status indicates an error or special condition.
     */
    @Column(name = "status_reason")
    private String statusReason;

    /**
     * Initializes default values before persisting a new enrollment link.
     * Sets:
     * <ul>
     *   <li>createdAt to current timestamp if not set</li>
     *   <li>sent to false if not set</li>
     *   <li>status to PENDING if not set</li>
     * </ul>
     * Also validates that expires_at is after created_at if both are present.
     *
     * @throws IllegalArgumentException if expires_at is before or equal to created_at
     */
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        // Ensure sent is false for newly created links
        this.sent = false;
        if (this.status == null) {
            this.status = EnrollmentStatus.PENDING;
        }
        validateExpirationDate();
    }

    /**
     * Validates expiration date before updating the entity.
     * Ensures that expires_at is after created_at if both are present.
     *
     * @throws IllegalArgumentException if expires_at is before or equal to created_at
     */
    @PreUpdate
    protected void onUpdate() {
        validateExpirationDate();
    }

    /**
     * Validates that expires_at is after created_at if both are present.
     *
     * @throws IllegalArgumentException if expires_at is before or equal to created_at
     */
    private void validateExpirationDate() {
        if (expiresAt != null && createdAt != null && !expiresAt.isAfter(createdAt)) {
            throw new IllegalArgumentException(
                    "expires_at must be after created_at. created_at: " + createdAt + ", expires_at: " + expiresAt
            );
        }
    }
}

