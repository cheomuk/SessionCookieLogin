package com.homework.session.controller;

import com.homework.session.dto.UserDto;
import com.homework.session.service.LoginService;
import com.homework.session.sessionManager.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LoginController {
    private final SessionManager sessionManager;
    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserDto userDto, BindingResult bindingResult,
                      HttpServletResponse response) {
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>("로그인에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }

        loginService.login(userDto.getEmail(), userDto.getPassword());

        UserDto saveDto = UserDto.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .build();

        sessionManager.createSession(saveDto, response);
        return new ResponseEntity<>("로그인에 성공했습니다.", HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        sessionManager.expire(request, response);
        return new ResponseEntity<>("로그아웃 되었습니다.", HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@Valid @RequestBody UserDto userDto) {
        return loginService.signup(userDto);
    }

    @PutMapping("/update")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
        return loginService.update(userDto);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestBody UserDto userDto) {
        return loginService.delete(userDto);
    }
}
