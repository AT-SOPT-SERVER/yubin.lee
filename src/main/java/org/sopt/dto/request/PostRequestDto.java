package org.sopt.dto.request;

public class PostRequestDto {

    private String title;

    public PostRequestDto(){}

    public PostRequestDto(String title){
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }
}
