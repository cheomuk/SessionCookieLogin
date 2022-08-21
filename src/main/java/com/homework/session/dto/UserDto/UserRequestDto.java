package com.homework.session.dto.UserDto;

import com.homework.session.entity.User;
import com.homework.session.enumcustom.UserRole;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,8}$", message = "닉네임은 특수문자를 제외한 2~8자리여야 합니다.")
    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;

    private String introduction;
    private UserRole userRole;
    private String email;

    public User toEntity() {
        User user = User.builder()
                .nickname(nickname)
                .introduction(introduction)
                .userRole(userRole)
                .build();

        return user;
    }
}
