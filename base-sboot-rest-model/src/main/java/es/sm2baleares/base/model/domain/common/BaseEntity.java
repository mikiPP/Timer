package es.sm2baleares.base.model.domain.common;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Base entity with the fields that all JPA entities may have
 */
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue
    private Long id;

}
