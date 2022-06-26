package com.homework.session.service;

import com.homework.session.Repository.UserRepository;
import com.homework.session.dto.UserDto;
import com.homework.session.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.hibernate.internal.CoreLogging.logger;

@RequiredArgsConstructor
@Transactional
@Service
public class LoginService {

    private final UserRepository userRepository;

    @Transactional
    public User login(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(u -> u.getPassword().equals(password))
                .orElse(null);
    }

    @Transactional
    public void signup(UserDto userDto) {
        User user = userDto.toEntity();

        if (checkEmailDuplicate(user.getEmail())) {
            logger("이미 등록된 이메일이 있습니다.");
        } else {
            userRepository.save(user);
        }
    }

    @Transactional
    public boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public Long update(Long id, UserDto userDto) {
        User user = userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("등록된 이메일이 없습니다."));
        user.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .build();
        return id;
    }
}
