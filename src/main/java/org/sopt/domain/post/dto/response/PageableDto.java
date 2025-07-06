package org.sopt.domain.post.dto.response;

import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
public record PageableDto(
        int page,
        int size,
        int totalPages,
        long totalElements,
        boolean last
) {
    public static PageableDto of(Page<?> page) {
        return PageableDto.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .last(page.isLast())
                .build();
    }
}
