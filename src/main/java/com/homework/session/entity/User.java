package com.homework.session.entity;

import com.homework.session.dto.UserDto.UserRequestDto;
import com.homework.session.enumcustom.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    @Column
    private String introduction;

    public void update(UserRequestDto userDto) {
        this.nickname = userDto.getNickname();
        this.userRole = userDto.getUserRole();
        this.introduction = userDto.getIntroduction();
    }

    public String getRoleKey() {
        return this.userRole.getKey();
    }
}
