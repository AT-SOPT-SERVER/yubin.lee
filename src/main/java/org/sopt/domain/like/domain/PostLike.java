package org.sopt.domain.like.domain;

import jakarta.persistence.*;
import lombok.*;
import org.sopt.domain.post.domain.Post;
import org.sopt.domain.user.domain.User;
import org.sopt.global.domain.BaseTimeEntity;

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
