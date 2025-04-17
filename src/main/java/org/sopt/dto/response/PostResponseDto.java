package org.sopt.dto.response;

import org.sopt.domain.Post;

import java.time.LocalDateTime;

public class PostResponseDto {

    private Long id;
    private String title;
    private LocalDateTime time;

    public PostResponseDto(Post post){
        this.id = post.getId();
        this.title = post.getTitle();
        this.time = post.getTime();
    }

    // Getter: Jackson -> JSON 변환할 때 필요하다
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
