package ar.edu.utn.frc.tup.lciii.templateSpring.entities.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * The {@code BaseEntity} class is a base class
 * that provides common auditing fields
 * for entities, such as the user who created
 * or last updated the entity, and the
 * respective timestamps.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    /**
     * The ID of the user who created the entity.
     */
    @Column(name = "created_user")
    private Long createdUser;

    /**
     * The date and time when the entity was created.
     */
    @Column(name = "created_date", updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    /**
     * The ID of the user who last updated the entity.
     */
    @Column(name = "last_updated_user")
    private Long lastUpdatedUser;

    /**
     * The date and time when the entity was last updated.
     */
    @Column(name = "last_updated_date")
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    /**
     * Field to handle soft delete (logical deletion).
     */
    @Column(name = "is_active")
    private Boolean isActive = true;
}
