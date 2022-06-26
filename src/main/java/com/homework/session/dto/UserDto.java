package com.homework.session.dto;

import com.homework.session.entity.User;
import lombok.Builder;

public class UserDto {
    private String email;
    private String password;
    private String phoneNumber;

    @Builder
    public UserDto(String email, String password, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public User toEntity(){
        return User.builder()
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .build();
    }
}
