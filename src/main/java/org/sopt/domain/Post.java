package org.sopt.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private final LocalDateTime time = LocalDateTime.now();

    public Post(){

    }

    public Post(String title){
        validateTitle(title);
        this.title = title;
    }

    // Getter 구현
    public Long getId(){
        return id;
    }

    public String getTitle(){
        return this.title;
    }

    public LocalDateTime getTime(){
        return this.time;
    }

    // Setter 구현
    public void setTitle(String title){
        validateTitle(title);
        this.title = title;
    }

    private void validateTitle(String title){
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("❌ 제목을 입력해야 합니다.");
        }
        if (title.length() > 30){
            throw new IllegalArgumentException("❌ 제목은 30자 이하여야 합니다.");
        }
    }

}
