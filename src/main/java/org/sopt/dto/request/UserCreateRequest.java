package org.sopt.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(@NotBlank(message = "닉네임은 필수입니다.") @Size(max = 10) String name, @NotBlank(message = "이메일은 필수입니다.") String email) {
}
