package org.sopt.domain.post.dto.response;

import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record PostAllResponseDto(
        List<PostListsDto> posts,
        PageableDto pageable
) {
    public static PostAllResponseDto of(Page<PostListsDto> postListsDto) {
        return PostAllResponseDto.builder()
                .posts(postListsDto.getContent())
                .pageable(PageableDto.of(postListsDto))
                .build();
    }
}
