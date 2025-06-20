package org.sopt.domain.post.domain;

import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.domain.comment.domain.PostComment;
import org.sopt.global.domain.BaseTimeEntity;
import org.sopt.domain.like.domain.PostLike;
import org.sopt.domain.user.domain.User;
import org.sopt.global.exception.CustomBadRequestException;
import org.sopt.global.enums.ErrorCode;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PostComment> postComments;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<PostLike> postLikes;

    public Post(User user, String title, String content){
        validateTitle(title);
        validateContent(content);
        this.user = user;
        this.title = title;
        this.content = content;
    }

    public void updateTitle(String title){
        validateTitle(title);
        this.title = title;
    }

    public void updateContent(String content){
        validateContent(content);
        this.content = content;
    }

    private void validateTitle(String title){
        if (title == null || title.trim().isEmpty()) {
            throw new CustomBadRequestException(ErrorCode.EMPTY_TITLE);
        }
        if (title.length() > 30){
            throw new CustomBadRequestException(ErrorCode.TITLE_TOO_LONG);
        }
    }

    private void validateContent(String content){
        if (content == null || content.trim().isEmpty()){
            throw new CustomBadRequestException(ErrorCode.EMPTY_CONTENT);
        }
    }


}
