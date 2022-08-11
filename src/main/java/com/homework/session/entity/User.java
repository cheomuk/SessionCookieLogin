package com.homework.session.entity;

import com.homework.session.dto.UserDto;
import com.homework.session.enumcustom.UserRole;
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
    private String nickname;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    @Column
    private String introduction;

    @Builder
    public User(String email, String nickname, String picture, UserRole userRole, String introduction) {
        this.email = email;
        this.nickname = nickname;
        this.picture = picture;
        this.userRole = userRole;
        this.introduction = introduction;
    }

    public void update(UserDto userDto) {
        this.nickname = userDto.getNickname();
        this.picture = userDto.getPicture();
        this.userRole = userDto.getUserRole();
        this.introduction = userDto.getIntroduction();
    }

    public String getRoleKey() {
        return this.userRole.getKey();
    }
}
