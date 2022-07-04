package com.homework.session.service;

import com.homework.session.Repository.UserRepository;
import com.homework.session.dto.UserDto;
import com.homework.session.entity.User;
import com.homework.session.error.exception.NotFoundException;
import com.homework.session.error.exception.UnauthorizedException;
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
    public ResponseEntity<UserDto> signup(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new UnauthorizedException("E0002", ACCESS_DENIED_EXCEPTION);
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
    public ResponseEntity<UserDto> update(UserDto userDto) {

        User user = userRepository.findByEmail(userDto.getEmail()).orElseThrow(() ->
                    { throw new UnauthorizedException("E0002", ACCESS_DENIED_EXCEPTION); });

        user.update(userDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        return new ResponseEntity<>(userDto, httpHeaders, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> delete(UserDto userDto) {
        Optional<User> user = userRepository.findByEmail(userDto.getEmail());
        if (user.isPresent()) {
            userRepository.delete(user.get());
        } else {
            throw new UnauthorizedException("E0002", ACCESS_DENIED_EXCEPTION);
        }
        return new ResponseEntity<>(userDto.getEmail() + " 가 정상적으로 탈퇴 처리 되었습니다.", HttpStatus.OK);
    }
}
