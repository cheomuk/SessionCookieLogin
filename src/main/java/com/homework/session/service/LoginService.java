package com.homework.session.service;

import com.homework.session.Repository.UserRepository;
import com.homework.session.dto.UserDto;
import com.homework.session.entity.User;
import com.homework.session.error.exception.UnAuthorizedException;
import com.homework.session.sessionManager.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.homework.session.error.ErrorCode.ACCESS_DENIED_EXCEPTION;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class LoginService {

    private final UserRepository userRepository;
    private final SessionManager sessionManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void login(String email, String password, HttpServletRequest request) {
        userRepository.findByEmail(email)
                .filter(u -> passwordEncoder.matches(password, u.getPassword()))
                .orElseThrow(() -> { throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION); });

        UserDto userDto = UserDto.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();

        HttpSession session = request.getSession();
        session.setAttribute("sessionId", userDto);
    }

    @Transactional
    public void signUp(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION);
        }

        User user = User.builder()
                .email(userDto.getEmail())
                .nickName(userDto.getNickName())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .phoneNumber(userDto.getPhoneNumber())
                .build();

        userRepository.save(user);
    }

    @Transactional
    public void update(UserDto userDto, HttpServletRequest request) {

        User user = userRepository.findByEmail(userDto.getEmail()).orElseThrow(() ->
                    { throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION); });

        UserDto updateDto = UserDto.builder()
                .email(userDto.getEmail())
                .nickName(userDto.getNickName())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .phoneNumber(userDto.getPhoneNumber())
                .build();

        HttpSession session = request.getSession();
        UserDto loginUser = (UserDto) session.getAttribute("sessionId");

        if ( loginUser != null ) {
            user.update(updateDto);
        }
    }

    @Transactional
    public void delete(UserDto userDto, HttpServletRequest request) {
        User user = userRepository.findByEmail(userDto.getEmail()).orElseThrow(() ->
            { throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION); });

        HttpSession session = request.getSession();
        UserDto loginUser = (UserDto) session.getAttribute("sessionId");

        if ( loginUser != null ) {
            userRepository.delete(user);
        }
    }
}
