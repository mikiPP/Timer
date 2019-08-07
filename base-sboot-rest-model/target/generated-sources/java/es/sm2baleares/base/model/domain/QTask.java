package es.sm2baleares.base.model.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTask is a Querydsl query type for Task
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QTask extends EntityPathBase<Task> {

    private static final long serialVersionUID = 817228236L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTask task = new QTask("task");

    public final es.sm2baleares.base.model.domain.common.QAuditedEntity _super = new es.sm2baleares.base.model.domain.common.QAuditedEntity(this);

    public final BooleanPath active = createBoolean("active");

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> creationDate = _super.creationDate;

    public final StringPath description = createString("description");

    public final NumberPath<Integer> duration = createNumber("duration", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> end_time = createDateTime("end_time", java.time.LocalDateTime.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModificationDate = _super.lastModificationDate;

    //inherited
    public final StringPath lastModifiedBy = _super.lastModifiedBy;

    public final StringPath name = createString("name");

    public final DateTimePath<java.time.LocalDateTime> start_time = createDateTime("start_time", java.time.LocalDateTime.class);

    public final QUser user;

    public QTask(String variable) {
        this(Task.class, forVariable(variable), INITS);
    }

    public QTask(Path<? extends Task> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTask(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTask(PathMetadata metadata, PathInits inits) {
        this(Task.class, metadata, inits);
    }

    public QTask(Class<? extends Task> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

