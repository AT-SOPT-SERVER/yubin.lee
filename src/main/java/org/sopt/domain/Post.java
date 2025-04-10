package org.sopt.domain;

import org.sopt.util.Validation;

public class Post {

    // private 선언하면 직접적으로 접근 불가
    private final int id;
    private String title;

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

    // Setter 구현
    public void setTitle(String title){
        Validation.isTitleValid(title);
        this.title = title;
    }

}
