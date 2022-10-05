package com.homework.session.service;

import com.homework.session.Repository.AuthRepository;
import com.homework.session.Repository.UserRepository;
import com.homework.session.dto.JwtDto.TokenResponse;
import com.homework.session.dto.UserDto.UserMyPageRequestDto;
import com.homework.session.dto.UserDto.UserRequestDto;
import com.homework.session.dto.UserDto.UserResponseDto;
import com.homework.session.entity.AuthEntity;
import com.homework.session.entity.User;
import com.homework.session.enumcustom.UserRole;
import com.homework.session.error.exception.UnAuthorizedException;
import com.homework.session.jwt.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Random;

import static com.homework.session.error.ErrorCode.ACCESS_DENIED_EXCEPTION;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class LoginService {

    private final UserRepository userRepository;
    private final KakaoAPI kakaoAPI;
    private final TokenUtils tokenUtils;
    private final AuthRepository authRepository;

    @Transactional
    public TokenResponse signUp(UserRequestDto userDto) {

        if (!userRepository.existsByNickname(userDto.getSerialCode())) {
            throw new UnAuthorizedException("식별코드가 일치하지 않습니다.", ACCESS_DENIED_EXCEPTION);
        } else if (userRepository.existsByNickname(userDto.getNickname())) {
            throw new UnAuthorizedException("중복된 닉네임입니다.", ACCESS_DENIED_EXCEPTION);
        }

        User user = userRepository.findByNickname(userDto.getSerialCode());
        user.update(userDto);

        user = userRepository.findByNickname(userDto.getNickname());

        String accessToken = tokenUtils.generateJwtToken(user);
        String refreshToken = tokenUtils.saveRefreshToken(user);

        authRepository.save(AuthEntity.builder().user(user).refreshToken(refreshToken).build());

        return TokenResponse.builder().ACCESS_TOKEN(accessToken).REFRESH_TOKEN(refreshToken).build();
    }

    @Transactional
    public TokenResponse checkUser(String code) {
        String access_token = kakaoAPI.getAccessToken(code);
        HashMap<String, Object> userInfo = kakaoAPI.getUserInfo(access_token);
        MultiValueMap<String, Object> sessionCarrier = new LinkedMultiValueMap<>();
        TokenResponse tokenResponse = new TokenResponse();
        String email = userInfo.get("email").toString();

        if (userRepository.existsByEmail(email)) {

            User user = userRepository.findByEmail(email).orElseThrow(() ->
                { throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION); });

            if (user.getIntroduction().equals("")) {
                userRepository.delete(user);
            } else {
                User userDto = User.builder()
                        .nickname(user.getNickname())
                        .email(email)
                        .introduction(user.getIntroduction())
                        .userRole(user.getUserRole())
                        .build();

                AuthEntity authEntity = authRepository.findByUserId(userDto.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Token 이 존재하지 않습니다."));

                String accessToken = "";
                String refreshToken = authEntity.getRefreshToken();

                if (tokenUtils.isValidRefreshToken(refreshToken)) {
                    accessToken = tokenUtils.generateJwtToken(authEntity.getUser());
                    tokenResponse.builder()
                            .ACCESS_TOKEN(accessToken)
                            .REFRESH_TOKEN(authEntity.getRefreshToken())
                            .build();
                } else {
                    accessToken = tokenUtils.generateJwtToken(authEntity.getUser());
                    refreshToken = tokenUtils.saveRefreshToken(user);
                    authEntity.refreshUpdate(refreshToken);
                    tokenResponse.builder().ACCESS_TOKEN(accessToken).REFRESH_TOKEN(refreshToken).build();
                }
            }
        } else {
            Random random = new Random();
            int checkNum = random.nextInt(888888) + 111111;
            String nickname = "ID" + checkNum;

            User userDto = User.builder()
                    .nickname(nickname)
                    .email(email)
                    .introduction("")
                    .userRole(UserRole.USER)
                    .build();

            userRepository.save(userDto);
        }
        return tokenResponse;
    }

    @Transactional
    public boolean checkNickname(String nickname) {
        boolean nicknameDuplicate = userRepository.existsByNickname(nickname);
        return !nicknameDuplicate;
    }

    @Transactional
    public void updateMyPage(UserMyPageRequestDto userDto, String nickname) {
        if (nickname == null) {
            throw new UnAuthorizedException("로그인이 필요합니다.", ACCESS_DENIED_EXCEPTION);
        }

        UserRequestDto myDto = UserRequestDto.builder()
                .nickname(userDto.getNickname())
                .userRole(userDto.getUserRole())
                .introduction(userDto.getIntroduction())
                .build();

        myDto.toEntity();
    }

    @Transactional
    public UserResponseDto viewMyPage(String nickname) {
        User user = userRepository.findByNickname(nickname);
        UserResponseDto userResponseDto = new UserResponseDto(user);
        return userResponseDto;
    }

    @Transactional
    public void delete(String nickname) {
        User user = userRepository.findByNickname(nickname);
        userRepository.delete(user);
    }
}
