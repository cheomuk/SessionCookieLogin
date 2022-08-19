package com.homework.session.Repository;

import com.homework.session.dto.UserDto.UserRequestDto;
import com.homework.session.entity.User;
import com.homework.session.enumcustom.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    User findByNickname(String nickname);
    boolean existsByNickname(String nickname);
    UserRequestDto findByNicknameAndUserRoleAndIntroduction(String nickname, UserRole role, String introduction);
}
