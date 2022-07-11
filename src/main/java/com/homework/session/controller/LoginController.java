package com.homework.session.controller;

import com.homework.session.dto.UserDto;
import com.homework.session.service.LoginService;
import com.homework.session.sessionManager.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LoginController {
    private final SessionManager sessionManager;
    private final LoginService loginService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public void login(@Valid @RequestBody UserDto userDto, HttpServletResponse response) {
        loginService.login(userDto.getEmail(), userDto.getPassword());

        UserDto saveDto = UserDto.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .build();

        sessionManager.createSession(saveDto, response);
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request) {
        sessionManager.expire(request);
    }

    @PostMapping("/signup")
    public void signUp(@Valid @RequestBody UserDto userDto) {
        loginService.signUp(userDto);
    }

    @PutMapping("/update")
    public void updateUser(@RequestBody UserDto userDto, HttpServletRequest request) {
        HttpSession session = request.getSession();

        if (session == sessionManager.getSession(request)) {
            loginService.update(userDto);
        }
    }

    @DeleteMapping("/delete")
    public void deleteUser(@RequestBody UserDto userDto, HttpServletRequest request) {
        HttpSession session = request.getSession();

        if (session == sessionManager.getSession(request)) {
            loginService.delete(userDto);
        }
    }
}
