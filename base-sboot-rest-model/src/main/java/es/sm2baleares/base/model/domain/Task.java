package es.sm2baleares.base.model.domain;

import es.sm2baleares.base.model.domain.common.AuditedEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDateTime;

@Audited
@Entity
@Table(name = "TASK", uniqueConstraints = @UniqueConstraint(columnNames = {"NAME", "USER"}))
@Getter
@Setter
@NoArgsConstructor
public class Task extends AuditedEntity {

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @JoinColumn(name = "USER", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "START_TIME", nullable = false)
    private LocalDateTime start_time;

    @Column(name = "END_TIME", nullable = false)
    private LocalDateTime end_time;

    @Column(name = "DURATION", nullable = false) // in miliseconds
    private Integer duration;


    @Column(name = "ACTIVE", columnDefinition = "boolean default true")
    private boolean active;


    @Override
    @PrePersist
    protected void onPersist() {

        super.setCreatedBy(this.getUser().getUsername());
        super.setCreationDate(LocalDateTime.now());
    }

    @Override
    @PreUpdate
    protected void onPreUpdate() {

        super.setLastModifiedBy(this.getUser().getUsername());
        super.setLastModificationDate(LocalDateTime.now());
    }


}
