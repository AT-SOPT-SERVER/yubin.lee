package org.sopt.domain.like.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPostCommentLike is a Querydsl query type for PostCommentLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPostCommentLike extends EntityPathBase<PostCommentLike> {

    private static final long serialVersionUID = -1393837859L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPostCommentLike postCommentLike = new QPostCommentLike("postCommentLike");

    public final org.sopt.global.domain.QBaseTimeEntity _super = new org.sopt.global.domain.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final org.sopt.domain.comment.domain.QPostComment postComment;

    public final org.sopt.domain.user.domain.QUser user;

    public QPostCommentLike(String variable) {
        this(PostCommentLike.class, forVariable(variable), INITS);
    }

    public QPostCommentLike(Path<? extends PostCommentLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPostCommentLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPostCommentLike(PathMetadata metadata, PathInits inits) {
        this(PostCommentLike.class, metadata, inits);
    }

    public QPostCommentLike(Class<? extends PostCommentLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.postComment = inits.isInitialized("postComment") ? new org.sopt.domain.comment.domain.QPostComment(forProperty("postComment"), inits.get("postComment")) : null;
        this.user = inits.isInitialized("user") ? new org.sopt.domain.user.domain.QUser(forProperty("user")) : null;
    }

}

