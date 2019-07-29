package es.sm2baleares.base.model.domain.common;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAuditedEntity is a Querydsl query type for AuditedEntity
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QAuditedEntity extends EntityPathBase<AuditedEntity> {

    private static final long serialVersionUID = -1595102847L;

    public static final QAuditedEntity auditedEntity = new QAuditedEntity("auditedEntity");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final StringPath createdBy = createString("createdBy");

    public final DateTimePath<java.time.LocalDateTime> creationDate = createDateTime("creationDate", java.time.LocalDateTime.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final DateTimePath<java.time.LocalDateTime> lastModificationDate = createDateTime("lastModificationDate", java.time.LocalDateTime.class);

    public final StringPath lastModifiedBy = createString("lastModifiedBy");

    public QAuditedEntity(String variable) {
        super(AuditedEntity.class, forVariable(variable));
    }

    public QAuditedEntity(Path<? extends AuditedEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAuditedEntity(PathMetadata metadata) {
        super(AuditedEntity.class, metadata);
    }

}

