package org.sopt.domain.comment.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.sopt.domain.post.domain.Post;
import org.sopt.global.domain.BaseTimeEntity;
import org.sopt.domain.like.domain.PostCommentLike;
import org.sopt.domain.user.domain.User;

import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(columnDefinition = "TEXT")
    @Size(min = 1, max = 300)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PostCommentLike> postCommentLikes;

    public void updateComment(String comment){
        this.comment = comment;
    }
}
