package org.sopt.domain;

import jakarta.persistence.*;
import org.sopt.exception.CustomBadRequestException;
import org.sopt.exception.ErrorCode;

@Entity
public class Post extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    protected Post(){

    }

    public Post(User user, String title, String content){
        validateTitle(title);
        validateContent(content);
        this.user = user;
        this.title = title;
        this.content = content;
    }

    // Getter 구현
    public Long getId(){
        return id;
    }

    public String getTitle(){
        return this.title;
    }

    public String getContent(){
        return this.content;
    }

    public User getUser(){
        return this.user;
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
