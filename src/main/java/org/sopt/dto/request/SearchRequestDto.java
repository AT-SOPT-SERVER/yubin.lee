package org.sopt.dto.request;

public class SearchRequestDto {

    private final String keyword;

    public SearchRequestDto(String keyword){
        this.keyword = keyword;
    }

    public String getKeyword(){
        return this.keyword;
    }
}
