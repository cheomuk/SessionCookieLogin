package com.homework.session.controller;

import com.homework.session.dto.JwtDto.CheckEnumRequest;
import com.homework.session.enumcustom.UserRole;
import com.homework.session.service.TokenService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "localhost:3000")
@Api(tags = {"토큰 검증 및 재발급 Controller"})
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/token/validate")
    public String validateToken(HttpServletRequest request, HttpServletResponse response) {
        return tokenService.validateToken(request, response);
    }

    @PostMapping("/check/writer")
    public boolean checkWriter(@RequestBody CheckEnumRequest requestDto, HttpServletRequest request) {
        return tokenService.checkWriter(requestDto, request);
    }

    @PostMapping("/check/enum")
    public UserRole checkEnum(HttpServletRequest request) {
        return tokenService.checkEnum(request);
    }
}
