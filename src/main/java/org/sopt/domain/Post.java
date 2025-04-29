package org.sopt.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private final LocalDateTime time = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Post(){

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

    public LocalDateTime getTime(){
        return this.time;
    }

    // Setter 구현
    public void setTitle(String title){
        validateTitle(title);
        this.title = title;
    }

    public void setContent(String content){
        validateContent(content);
        this.content = content;
    }

    private void validateTitle(String title){
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("제목을 입력해야 합니다.");
        }
        if (title.length() > 30){
            throw new IllegalArgumentException("제목은 30자 이하여야 합니다.");
        }
    }

    private void validateContent(String content){
        if (content == null || content.trim().isEmpty()){
            throw new IllegalArgumentException("내용을 입력해야 합니다.");
        }
    }

}
