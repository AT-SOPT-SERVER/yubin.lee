package org.sopt.domain.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 34706776L;

    public static final QUser user = new QUser("user");

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath loginId = createString("loginId");

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final ListPath<org.sopt.domain.comment.domain.PostComment, org.sopt.domain.comment.domain.QPostComment> postComments = this.<org.sopt.domain.comment.domain.PostComment, org.sopt.domain.comment.domain.QPostComment>createList("postComments", org.sopt.domain.comment.domain.PostComment.class, org.sopt.domain.comment.domain.QPostComment.class, PathInits.DIRECT2);

    public final ListPath<org.sopt.domain.post.domain.Post, org.sopt.domain.post.domain.QPost> posts = this.<org.sopt.domain.post.domain.Post, org.sopt.domain.post.domain.QPost>createList("posts", org.sopt.domain.post.domain.Post.class, org.sopt.domain.post.domain.QPost.class, PathInits.DIRECT2);

    public final EnumPath<Role> role = createEnum("role", Role.class);

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

