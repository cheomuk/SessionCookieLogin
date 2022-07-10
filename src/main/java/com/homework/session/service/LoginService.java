package com.homework.session.service;

import com.homework.session.Repository.UserRepository;
import com.homework.session.dto.UserDto;
import com.homework.session.entity.User;
import com.homework.session.error.exception.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.homework.session.error.ErrorCode.ACCESS_DENIED_EXCEPTION;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class LoginService {

    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void login(String email, String password) {
        userRepository.findByEmail(email)
                .filter(u -> passwordEncoder.matches(u.getPassword(), password))
                .orElse(null );
    }

    @Transactional
    public void signUp(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION);
        }

        User user = User.builder()
                        .email(userDto.getEmail())
                        .password(passwordEncoder.encode(userDto.getPassword()))
                        .phoneNumber(userDto.getPhoneNumber())
                        .build();

        userRepository.save(user);
    }

    @Transactional
    public void update(UserDto userDto) {

        User user = userRepository.findByEmail(userDto.getEmail()).orElseThrow(() ->
                    { throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION); });

        UserDto updateDto = UserDto.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .phoneNumber(userDto.getPhoneNumber())
                .build();

        user.update(updateDto);
    }

    @Transactional
    public void delete(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail()).orElseThrow(() ->
            { throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION); });

        log.info("탈퇴처리 되었습니다.");
        userRepository.delete(user);
    }
}
