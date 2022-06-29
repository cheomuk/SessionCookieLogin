package com.homework.session.controller;

import com.homework.session.dto.UserDto;
import com.homework.session.entity.User;
import com.homework.session.service.LoginService;
import com.homework.session.sessionManager.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public String login(@Valid @ModelAttribute User user, BindingResult bindingResult,
                      HttpServletResponse response) {
        if (bindingResult.hasErrors()){
            return "redirect:/";
        }

        User loginUser = loginService.login(user.getEmail(), user.getPassword());

        if (loginUser == null){
            bindingResult.reject("loginFail", "아이디 또는 비번이 일치하지 않습니다.");
        }

        sessionManager.createSession(loginUser, response);
        return "/loginHome";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        sessionManager.expire(request);
        return "redirect:/";
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@Valid @RequestBody UserDto userDto) {
        return loginService.signup(userDto);
    }

    @PutMapping("/update/{email}")
    public ResponseEntity<UserDto> updateUser(@Valid @PathVariable String email, @RequestBody UserDto userDto) {
        return loginService.update(email, userDto);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable String email) {
        return loginService.delete(email);
    }
}
