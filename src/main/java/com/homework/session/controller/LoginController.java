package com.homework.session.controller;

import com.homework.session.config.LoginUser;
import com.homework.session.dto.UserDto.UserRequestDto;
import com.homework.session.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "localhost:3000")
public class LoginController {

    private final LoginService loginService;
    private final HttpSession httpSession;

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        httpSession.invalidate();
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    @GetMapping("/check/user")
    public MultiValueMap<String, String> checkUser(@RequestParam String code, HttpServletRequest request) {
        return loginService.checkUser(code, request);
    }

    @PostMapping("/signup/first")
    public ResponseEntity signUp(@RequestBody UserRequestDto userDto, HttpServletRequest request) {
        return ResponseEntity.ok(loginService.signUp(userDto, request));
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
