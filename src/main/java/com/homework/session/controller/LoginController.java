package com.homework.session.controller;

import com.homework.session.config.LoginUser;
import com.homework.session.dto.UserDto;
import com.homework.session.service.CustomOAuth2UserService;
import com.homework.session.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final HttpSession httpSession;

    @PostMapping("/oauth2/authorization/kakao")
    public String login(OAuth2UserRequest userRequest) {
        customOAuth2UserService.loadUser(userRequest);

        return "/signup";
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request) {
        httpSession.invalidate();
    }

    @PostMapping("/signup")
    public void signUp(@RequestBody UserDto userDto) {
        loginService.signUp(userDto);
    }

    @PostMapping("/signup/checkbox")
    public boolean checkNickname(String nickname) {
        return loginService.checkNickname(nickname);
    }

    @PutMapping("/update")
    public void updateUser(@RequestBody UserDto userDto, @LoginUser UserDto loginUser) {
        loginService.update(userDto, loginUser);
    }

    @DeleteMapping("/delete")
    public void deleteUser(@RequestBody UserDto userDto, @LoginUser UserDto loginUser) {
        loginService.delete(userDto, loginUser);
    }
}
