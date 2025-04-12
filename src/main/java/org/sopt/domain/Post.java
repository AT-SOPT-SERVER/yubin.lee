package org.sopt.domain;

import org.sopt.util.Validation;

import java.time.LocalDateTime;

public class Post {

    // private 선언하면 직접적으로 접근 불가
    private final int id;
    private String title;
    private final LocalDateTime time = LocalDateTime.now();

    public Post(int id, String title){
        Validation.isTitleValid(title);
        this.id = id;
        this.title = title;
    }

    // Getter 구현
    public int getId(){
        return this.id;
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
