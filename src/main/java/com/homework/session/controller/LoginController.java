package com.homework.session.controller;

import com.homework.session.config.NoAuth;
import com.homework.session.dto.JwtDto.TokenResponse;
import com.homework.session.dto.UserDto.UserMyPageRequestDto;
import com.homework.session.dto.UserDto.UserRequestDto;
import com.homework.session.dto.UserDto.UserResponseDto;
import com.homework.session.jwt.JwtTokenProvider;
import com.homework.session.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "localhost:3000")
@Api(tags = {"로그인 Controller"})
public class LoginController {

    private final LoginService loginService;
    private final JwtTokenProvider jwtTokenProvider;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginUser", value = "로그인 세션값", required = true,
                    dataType = "Object", paramType = "query")
    })
    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "카카오 인증코드", required = true,
                    dataType = "String", paramType = "query")
    })
    @GetMapping("/check/user")
    public MultiValueMap<String, Object> checkUser(@RequestParam String code, HttpServletResponse response) {
        return loginService.checkUser(code, response);
    }


    @PostMapping("/signup")
    public TokenResponse signUp(@RequestBody UserRequestDto userDto, HttpServletResponse response) {
        return loginService.signUp(userDto, response);
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "nickname", value = "nickname 값", required = true,
                    dataType = "String", paramType = "query")
    })
    @GetMapping("/signup/checkbox")
    public boolean checkNickname(String nickname) {
        return loginService.checkNickname(nickname);
    }

    @PostMapping("/test/token")
    public TokenResponse testToken(@RequestBody UserRequestDto userRequestDto, HttpServletResponse response) {
        return loginService.TestToken(userRequestDto, response);
    }


//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "loginUser", value = "로그인 세션값", required = true,
//                    dataType = "Object", paramType = "query")
//    })
//    @PutMapping("/mypage")
//    public UserResponseDto viewMyPage(@ApiIgnore @NoAuth TokenResponse tokenResponse) {
//        return loginService.viewMyPage(tokenResponse.getAccessToken());
//    }
//
//
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "loginUser", value = "로그인 세션값", required = true,
//                    dataType = "Object", paramType = "query")
//    })
//    @PutMapping("/mypage/update")
//    public ResponseEntity<String> updateMyPage(@RequestBody UserMyPageRequestDto userDto,
//                                               @ApiIgnore @NoAuth TokenResponse tokenResponse) {
//        loginService.updateMyPage(userDto, tokenResponse.getAccessToken());
//        return ResponseEntity.ok("회원정보가 수정되었습니다.");
//    }
//
//
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "loginUser", value = "로그인 세션값", required = true,
//                    dataType = "Object", paramType = "query")
//    })
//    @DeleteMapping("/mypage/delete")
//    public ResponseEntity<String> deleteUser(@NoAuth TokenResponse tokenResponse) {
//        loginService.delete(tokenResponse.getAccessToken());
//        return ResponseEntity.ok("회원탈퇴 처리 되었습니다.");
//    }
}
