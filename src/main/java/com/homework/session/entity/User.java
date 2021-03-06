package com.homework.session.entity;

import com.homework.session.dto.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "user_profile")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickName;

    @Column(nullable = false, length = 60)
    private String password;

    @Column
    private String phoneNumber;

    @Builder
    public User(String email, String nickName, String password, String phoneNumber) {
        this.email = email;
        this.nickName = nickName;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public void update(UserDto userDto) {
        this.email = userDto.getEmail();
        this.nickName = userDto.getNickName();
        this.password = userDto.getPassword();
        this.phoneNumber = userDto.getPhoneNumber();
    }
}
