package com.homework.session.dto;

import com.homework.session.entity.User;
import com.homework.session.enumcustom.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {

    private String nickname;
    private String email;
    private String picture;
    private String introduction;
    private UserRole userRole;

    public UserDto(User user) {
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.picture = user.getPicture();
        this.introduction = user.getIntroduction();
        this.userRole = user.getUserRole();
    }
}
