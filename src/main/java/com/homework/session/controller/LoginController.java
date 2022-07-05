package com.homework.session.controller;

import com.homework.session.dto.UserDto;
import com.homework.session.error.exception.BadRequestException;
import com.homework.session.service.LoginService;
import com.homework.session.sessionManager.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.homework.session.error.ErrorCode.RUNTIME_EXCEPTION;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LoginController {
    private final SessionManager sessionManager;
    private final LoginService loginService;

    @PostMapping("/login")
    public void login(@Valid @RequestBody UserDto userDto, BindingResult bindingResult,
                      HttpServletResponse response) {
        if (bindingResult.hasErrors()){
            throw new BadRequestException("E0001", RUNTIME_EXCEPTION);
        }

        loginService.login(userDto.getEmail(), userDto.getPassword());

        UserDto saveDto = UserDto.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .build();

        sessionManager.createSession(saveDto, response);
        log.info("login success");
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        sessionManager.expire(request, response);
        log.info("logout success");
    }

    @PostMapping("/signup")
    public UserDto signUp(@Valid @RequestBody UserDto userDto) {
        return loginService.signup(userDto);
    }

    @PutMapping("/update")
    public UserDto updateUser(@RequestBody UserDto userDto) {
        return loginService.update(userDto);
    }

    @DeleteMapping("/delete")
    public UserDto deleteUser(@RequestBody UserDto userDto) {
        log.info("delete success");
        return loginService.delete(userDto);
    }
}
