package org.sopt.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.sopt.util.Validation;

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
        Validation.isTitleValid(title);
        this.title = title;
    }

}
