package es.sm2baleares.base.model.domain.common;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

/**
 * Audited entity base class
 * <p>
 * This class adds the following fields: createdBy, creationDate, lastModificationDate
 * <p>
 * These fields are feeded using the @PrePersist and @PreUpdate event listeners.
 *
 * @see <a href="https://docs.oracle.com/cd/E16439_01/doc.1013/e13981/undejbs003.htm#BABDFIDJ">JPA Documention</a>
 */
@MappedSuperclass
@Audited
@Getter
@Setter
public abstract class AuditedEntity extends BaseEntity {

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;

    @Column(name = "LAST_MODIFIED_BY")
    private String lastModifiedBy;

    @Column(name = "LAST_MODIFICATION_DATE")
    private LocalDateTime lastModificationDate;

    /**
     * Listener method executed before persisting a new entity
     */
    @PrePersist
    protected void onPersist() {

        this.createdBy = getUsername();
        this.creationDate = LocalDateTime.now();
    }

    /**
     * Listener method executed before updating an entity
     */
    @PreUpdate
    protected void onPreUpdate() {

        this.lastModifiedBy = getUsername();
        this.lastModificationDate = LocalDateTime.now();
    }

    private String getUsername() {

        // Get user (if present)
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if (requestAttributes != null && ((ServletRequestAttributes) requestAttributes).getRequest() != null) {

            return (String) ((ServletRequestAttributes) requestAttributes).getRequest().getAttribute("username");
        }

        return null;

    }

}
