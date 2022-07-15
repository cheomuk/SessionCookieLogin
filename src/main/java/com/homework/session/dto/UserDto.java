package com.homework.session.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String email;
    private String nickName;
    private String password;
    private String phoneNumber;

    public UserDto(UserDto userDto) {
        this.email = userDto.getEmail();
        this.nickName = userDto.getNickName();
        this.password = userDto.getPassword();
        this.phoneNumber = userDto.getPhoneNumber();
    }
}
