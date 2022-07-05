package com.homework.session.service;

import com.homework.session.Repository.UserRepository;
import com.homework.session.dto.UserDto;
import com.homework.session.entity.User;
import com.homework.session.error.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.homework.session.error.ErrorCode.ACCESS_DENIED_EXCEPTION;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class LoginService {

    private final UserRepository userRepository;

    @Transactional
    public void login(String email, String password) {
        userRepository.findByEmail(email)
                .filter(u -> u.getPassword().equals(password))
                .orElseThrow(() -> new UnauthorizedException("E0002", ACCESS_DENIED_EXCEPTION) );
    }

    @Transactional
    public UserDto signup(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new UnauthorizedException("E0002", ACCESS_DENIED_EXCEPTION);
        }

        User user = User.builder()
                        .email(userDto.getEmail())
                        .password(userDto.getPassword())
                        .phoneNumber(userDto.getPhoneNumber())
                        .build();

        userRepository.save(user);

        return UserDto.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    @Transactional
    public UserDto update(UserDto userDto) {

        User user = userRepository.findByEmail(userDto.getEmail()).orElseThrow(() ->
                    { throw new UnauthorizedException("E0002", ACCESS_DENIED_EXCEPTION); });

        user.update(userDto);

        return UserDto.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    @Transactional
    public UserDto delete(UserDto userDto) {
        Optional<User> user = userRepository.findByEmail(userDto.getEmail());
        if (!user.isPresent()) {
            throw new UnauthorizedException("E0002", ACCESS_DENIED_EXCEPTION);
        }

        log.info("탈퇴처리 되었습니다.");
        userRepository.delete(user.get());

        return UserDto.builder()
                .email(userDto.getEmail())
                .build();
    }
}
