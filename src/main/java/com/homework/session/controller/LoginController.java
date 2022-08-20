package com.homework.session.controller;

import com.homework.session.Repository.UserRepository;
import com.homework.session.config.LoginUser;
import com.homework.session.dto.UserDto.UserRequestDto;
import com.homework.session.service.CustomOAuth2UserService;
import com.homework.session.service.KakaoAPI;
import com.homework.session.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final UserRepository userRepository;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final KakaoAPI kakaoAPI;
    private final HttpSession httpSession;

    @GetMapping("/oauth2/authorization/kakao")
    public ResponseEntity<String> login(OAuth2UserRequest userRequest) {
        customOAuth2UserService.loadUser(userRequest);

        return ResponseEntity.ok("로그인 성공");
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        httpSession.invalidate();
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    @PostMapping("/signup/first")
    public ResponseEntity signUp(@RequestBody UserRequestDto userDto) {
        return ResponseEntity.ok(loginService.signUp(userDto));
    }

    @PostMapping("/signup/checkbox")
    public boolean checkNickname(String nickname) {
        return loginService.checkNickname(nickname);
    }


    @PutMapping("/mypage/update")
    public ResponseEntity<String> myPage(@RequestBody UserRequestDto userDto, @LoginUser UserRequestDto loginUser) {
        loginService.myPage(userDto, loginUser);
        return ResponseEntity.ok("회원정보가 수정되었습니다.");
    }

    @DeleteMapping("/mypage/delete")
    public ResponseEntity<String> deleteUser(String nickname, @LoginUser UserRequestDto loginUser) {
        loginService.delete(nickname, loginUser);
        return ResponseEntity.ok("회원탈퇴 처리 되었습니다.");
    }
}
