package com.homework.session.dto.UserDto;

import com.homework.session.entity.User;
import com.homework.session.enumcustom.UserRole;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class UserResponseDto implements Serializable {

    private String nickname;
    private String introduction;
    private UserRole userRole;

    public UserResponseDto(User user) {
        this.nickname = user.getNickname();
        this.introduction = user.getIntroduction();
        this.userRole = user.getUserRole();
    }
}
