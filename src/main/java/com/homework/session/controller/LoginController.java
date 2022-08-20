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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        httpSession.invalidate();
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    @GetMapping("/check/user")
    public MultiValueMap<String, Object> checkUser(String token) {
        String access_token = kakaoAPI.getAccessToken(token);
        HashMap<String, Object> userInfo = kakaoAPI.getUserInfo(access_token);
        MultiValueMap<String, Object> sessionCarrier = new LinkedMultiValueMap<>();
        String email = userInfo.get("email").toString();

        if (userRepository.existsByEmail(email)) {
            httpSession.setAttribute("user", email);
            sessionCarrier.add("session", httpSession.getAttribute(email));
            sessionCarrier.add("message", "이미 가입한 회원입니다.");
            return sessionCarrier;
        } else {
            sessionCarrier.add("message", "처음 방문한 회원입니다.");
            return sessionCarrier;
        }
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
