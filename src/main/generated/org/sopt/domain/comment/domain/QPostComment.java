package org.sopt.domain.comment.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPostComment is a Querydsl query type for PostComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPostComment extends EntityPathBase<PostComment> {

    private static final long serialVersionUID = 788548586L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPostComment postComment = new QPostComment("postComment");

    public final org.sopt.global.domain.QBaseTimeEntity _super = new org.sopt.global.domain.QBaseTimeEntity(this);

    public final StringPath comment = createString("comment");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final org.sopt.domain.post.domain.QPost post;

    public final ListPath<org.sopt.domain.like.domain.PostCommentLike, org.sopt.domain.like.domain.QPostCommentLike> postCommentLikes = this.<org.sopt.domain.like.domain.PostCommentLike, org.sopt.domain.like.domain.QPostCommentLike>createList("postCommentLikes", org.sopt.domain.like.domain.PostCommentLike.class, org.sopt.domain.like.domain.QPostCommentLike.class, PathInits.DIRECT2);

    public final org.sopt.domain.user.domain.QUser user;

    public QPostComment(String variable) {
        this(PostComment.class, forVariable(variable), INITS);
    }

    public QPostComment(Path<? extends PostComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPostComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPostComment(PathMetadata metadata, PathInits inits) {
        this(PostComment.class, metadata, inits);
    }

    public QPostComment(Class<? extends PostComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.post = inits.isInitialized("post") ? new org.sopt.domain.post.domain.QPost(forProperty("post"), inits.get("post")) : null;
        this.user = inits.isInitialized("user") ? new org.sopt.domain.user.domain.QUser(forProperty("user")) : null;
    }

}

