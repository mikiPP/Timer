package es.sm2baleares.base.model.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 817274898L;

    public static final QUser user = new QUser("user");

    public final es.sm2baleares.base.model.domain.common.QAuditedEntity _super = new es.sm2baleares.base.model.domain.common.QAuditedEntity(this);

    public final BooleanPath active = createBoolean("active");

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> creationDate = _super.creationDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModificationDate = _super.lastModificationDate;

    //inherited
    public final StringPath lastModifiedBy = _super.lastModifiedBy;

    public final StringPath password = createString("password");

    public final SetPath<Task, QTask> tasks = this.<Task, QTask>createSet("tasks", Task.class, QTask.class, PathInits.DIRECT2);

    public final StringPath username = createString("username");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

