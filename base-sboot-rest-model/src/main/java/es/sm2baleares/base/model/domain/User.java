package es.sm2baleares.base.model.domain;

import es.sm2baleares.base.model.domain.common.AuditedEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * User Entity
 */

@Audited
@Entity
@Table(name = "USER")
@Getter
@Setter
@NoArgsConstructor
public class User extends AuditedEntity {

    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    private Set<Task> tasks;

    @Column(name = "ACTIVE", columnDefinition = "boolean default true")
    private boolean active;


    public User(String username, String password) {
        this.password = password;
        this.username = username;
    }

    @Override
    @PrePersist
    protected void onPersist() {

        super.setCreatedBy(this.getUsername());
        super.setCreationDate(LocalDateTime.now());
    }

    @Override
    @PreUpdate
    protected void onPreUpdate() {

        super.setLastModifiedBy(this.getUsername());
        super.setLastModificationDate(LocalDateTime.now());
    }

}
