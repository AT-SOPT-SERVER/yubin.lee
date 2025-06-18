package org.sopt.domain.like.model;

import jakarta.persistence.*;
import lombok.*;
import org.sopt.domain.post.model.Post;
import org.sopt.domain.user.model.User;
import org.sopt.global.BaseTimeEntity;

@Entity
@Builder
@Table(
        indexes = {
                @Index(name = "idx_post_id", columnList = "post_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_post", columnNames = {"user_id", "post_id"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostLike extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
