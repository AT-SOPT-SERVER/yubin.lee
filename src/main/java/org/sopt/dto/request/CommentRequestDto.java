package org.sopt.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CommentRequestDto(
        @Size(min = 1, max = 300, message = "댓글은 300자 이내로 입력해야 합니다.")
        String comment
) {
}
