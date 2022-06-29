package com.homework.session.service;

import com.homework.session.Repository.UserRepository;
import com.homework.session.dto.UserDto;
import com.homework.session.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class LoginService {

    private final UserRepository userRepository;

    @Transactional
    public User login(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(u -> u.getPassword().equals(password))
                .orElse(null);
    }

    @Transactional
    public ResponseEntity<UserDto> signup(UserDto userDto) {
        if (checkEmailDuplicate(userDto.getEmail())) {
            throw new RuntimeException("이미 등록된 이메일이 있습니다.");
        }

        User user = User.builder()
                        .email(userDto.getEmail())
                        .password(userDto.getPassword())
                        .phoneNumber(userDto.getPhoneNumber())
                        .build();

        userRepository.save(user);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        return new ResponseEntity<>(userDto, httpHeaders, HttpStatus.CREATED);
    }

    @Transactional
    public boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public ResponseEntity<UserDto> update(String email, UserDto userDto) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User Not Found");
        }

        User user = userOpt.get();

        user.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .phoneNumber(userDto.getPhoneNumber())
                .build();

        userRepository.save(user);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        return new ResponseEntity<>(userDto, httpHeaders, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> delete(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            userRepository.delete(user.get());
        } else {
            throw new RuntimeException("회원이 아닙니다.");
        }
        return new ResponseEntity<>(email + " 가 정상적으로 탈퇴 처리 되었습니다.", HttpStatus.OK);
    }
}
