package org.sopt.domain.like.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.sopt.domain.post.model.PostComment;
import org.sopt.domain.user.model.User;
import org.sopt.global.BaseTimeEntity;

@Entity
@Table(
        indexes = {
                @Index(name = "idx_post_comment_id", columnList = "post_comment_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_post", columnNames = {"user_id", "post_comment_id"})
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostCommentLike extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_comment_id")
    private PostComment postComment;
}
