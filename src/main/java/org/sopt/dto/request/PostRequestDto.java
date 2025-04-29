package org.sopt.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.sopt.domain.Post;
import org.sopt.domain.User;

public record PostRequestDto(
        @NotBlank(message = "제목을 입력해야 합니다.")
        @Size(max = 30, message = "제목은 30자 이하여야 합니다.")
        String title, @NotBlank(message = "내용을 입력해야 합니다.") String content
) {
        public Post toEntity(User user){
                return new Post(user, this.title, this.content());
        }
}