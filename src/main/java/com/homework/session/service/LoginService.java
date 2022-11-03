package com.homework.session.service;

import com.homework.session.Repository.UserRepository;
import com.homework.session.dto.JwtDto.TokenResponse;
import com.homework.session.dto.UserDto.UserMyPageRequestDto;
import com.homework.session.dto.UserDto.UserRequestDto;
import com.homework.session.dto.UserDto.UserResponseDto;
import com.homework.session.entity.User;
import com.homework.session.enumcustom.UserRole;
import com.homework.session.error.exception.UnAuthorizedException;
import com.homework.session.jwt.JwtTokenProvider;
import com.homework.session.service.Jwt.RedisService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    @Transactional
    public MultiValueMap<String, Object> signUp(UserRequestDto userDto, HttpServletResponse response) {

        if (!userRepository.existsByNickname(userDto.getSerialCode())) {
            throw new UnAuthorizedException("식별코드가 일치하지 않습니다.", ACCESS_DENIED_EXCEPTION);
        } else if (userRepository.existsByNickname(userDto.getNickname())) {
            throw new UnAuthorizedException("중복된 닉네임입니다.", ACCESS_DENIED_EXCEPTION);
        }

        MultiValueMap<String, Object> sessionCarrier = new LinkedMultiValueMap<>();

        User user = userRepository.findByNickname(userDto.getSerialCode());
        user.update(userDto);

        user = userRepository.findByNickname(userDto.getNickname());
        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail(), user.getRoles());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail(), user.getRoles());

        jwtTokenProvider.setHeaderAccessToken(response, accessToken);
        jwtTokenProvider.setHeaderRefreshToken(response, refreshToken);

        redisService.setValues(refreshToken, user.getEmail());
        sessionCarrier.add("message", "회원가입 성공");

        return sessionCarrier;
    }

    @Transactional
    public MultiValueMap<String, Object> checkUser(String code, HttpServletResponse response) {
        String access_token = kakaoAPI.getAccessToken(code);
        HashMap<String, Object> userInfo = kakaoAPI.getUserInfo(access_token);

        MultiValueMap<String, Object> sessionCarrier = new LinkedMultiValueMap<>();
        String email = userInfo.get("email").toString();

        if (userRepository.existsByEmail(email)) {

            User user = userRepository.findByEmail(email).orElseThrow(() ->
                { throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION); });

            if (user.getIntroduction().equals("")) {
                userRepository.delete(user);
                sessionCarrier.add("fail", true);
            } else {

                String accessToken = jwtTokenProvider.createAccessToken(user.getEmail(), user.getRoles());
                String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail(), user.getRoles());

                jwtTokenProvider.setHeaderAccessToken(response, accessToken);
                jwtTokenProvider.setHeaderRefreshToken(response, refreshToken);

                redisService.setValues(refreshToken, user.getEmail());

                sessionCarrier.add("message", "로그인 성공");
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
            sessionCarrier.add("SerialCode", nickname);
        }
        return sessionCarrier;
    }

    @Transactional
    public boolean checkNickname(String nickname) {
        boolean nicknameDuplicate = userRepository.existsByNickname(nickname);
        return !nicknameDuplicate;
    }

    @Transactional
    public TokenResponse createToken(UserRequestDto userRequestDto, HttpServletResponse response) {

        if (userRepository.existsByNickname(userRequestDto.getNickname())) {
            throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION);
        }

        User user = User.builder()
                .email("evan05@gmail.com")
                .introduction(userRequestDto.getIntroduction())
                .userRole(userRequestDto.getUserRole())
                .nickname(userRequestDto.getNickname())
                .build();

        userRepository.save(user);

        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail(), user.getRoles());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail(), user.getRoles());

        jwtTokenProvider.setHeaderAccessToken(response, accessToken);
        jwtTokenProvider.setHeaderRefreshToken(response, refreshToken);

        redisService.setValues(refreshToken, user.getEmail());

        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return tokenResponse;
    }

    @Transactional
    public String resolverToken(UserMyPageRequestDto requestDto,
                                HttpServletRequest request, HttpServletResponse response) {

        String authorization = jwtTokenProvider.resolveAccessToken(request);
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);

        String email = jwtTokenProvider.getUserEmail(refreshToken);

        User user = userRepository.findByEmail(email).orElseThrow(() ->
        { throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION); });

        UserRequestDto updateUser = UserRequestDto.builder()
                .nickname(requestDto.getNickname())
                .userRole(requestDto.getUserRole())
                .introduction(requestDto.getIntroduction())
                .build();

        user.update(updateUser);
        return "내 정보 업데이트 완료";
    }

//    @Transactional
//    public void updateMyPage(UserMyPageRequestDto userDto, String token) {
//        if (tokenService.getNickname(token).isEmpty()) {
//            throw new UnAuthorizedException("로그인이 필요합니다.", ACCESS_DENIED_EXCEPTION);
//        }
//
//        UserRequestDto myDto = UserRequestDto.builder()
//                .nickname(userDto.getNickname())
//                .userRole(userDto.getUserRole())
//                .introduction(userDto.getIntroduction())
//                .build();
//
//        myDto.toEntity();
//    }
//
//    @Transactional
//    public UserResponseDto viewMyPage(String token) {
//        User user = userRepository.findByNickname(tokenService.getNickname(token));
//        UserResponseDto userResponseDto = new UserResponseDto(user);
//        return userResponseDto;
//    }
//
//    @Transactional
//    public void delete(String token) {
//        User user = userRepository.findByNickname(tokenService.getNickname(token));
//        userRepository.delete(user);
//    }
}
