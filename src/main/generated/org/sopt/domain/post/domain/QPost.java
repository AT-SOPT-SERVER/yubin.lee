package org.sopt.domain.post.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = 1777293186L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final org.sopt.global.domain.QBaseTimeEntity _super = new org.sopt.global.domain.QBaseTimeEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final ListPath<org.sopt.domain.comment.domain.PostComment, org.sopt.domain.comment.domain.QPostComment> postComments = this.<org.sopt.domain.comment.domain.PostComment, org.sopt.domain.comment.domain.QPostComment>createList("postComments", org.sopt.domain.comment.domain.PostComment.class, org.sopt.domain.comment.domain.QPostComment.class, PathInits.DIRECT2);

    public final ListPath<org.sopt.domain.like.domain.PostLike, org.sopt.domain.like.domain.QPostLike> postLikes = this.<org.sopt.domain.like.domain.PostLike, org.sopt.domain.like.domain.QPostLike>createList("postLikes", org.sopt.domain.like.domain.PostLike.class, org.sopt.domain.like.domain.QPostLike.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    public final org.sopt.domain.user.domain.QUser user;

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new org.sopt.domain.user.domain.QUser(forProperty("user")) : null;
    }

}

