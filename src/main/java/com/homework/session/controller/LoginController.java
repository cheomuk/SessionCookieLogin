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
    public void login(@Valid @RequestBody UserDto userDto, HttpServletRequest request) {
        loginService.login(userDto.getEmail(), userDto.getPassword(), request);
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
    }

    @PostMapping("/signup")
    public void signUp(@Valid @RequestBody UserDto userDto) {
        loginService.signUp(userDto);
    }

    @PutMapping("/update")
    public void updateUser(@RequestBody UserDto userDto, HttpServletRequest request) {
        loginService.update(userDto, request);
    }

    @DeleteMapping("/delete")
    public void deleteUser(@RequestBody UserDto userDto, HttpServletRequest request) {
            loginService.delete(userDto, request);
    }
}
