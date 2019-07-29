package es.sm2baleares.base.model.domain.audit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "USER_REVISION_ENTITY")
@RevisionEntity(UserRevisionListener.class)
@Getter
@Setter
@NoArgsConstructor
public class UserRevisionEntity extends DefaultRevisionEntity {

    private String username;

}