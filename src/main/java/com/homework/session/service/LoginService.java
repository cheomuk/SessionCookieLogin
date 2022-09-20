package com.homework.session.service;

import com.homework.session.Repository.UserRepository;
import com.homework.session.dto.UserDto.UserMyPageRequestDto;
import com.homework.session.dto.UserDto.UserRequestDto;
import com.homework.session.dto.UserDto.UserResponseDto;
import com.homework.session.entity.User;
import com.homework.session.enumcustom.UserRole;
import com.homework.session.error.exception.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpSession;
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
    private final HttpSession httpSession;

    @Transactional
    public MultiValueMap<String, Object> signUp(UserRequestDto userDto) {

        MultiValueMap<String, Object> sessionCarrier = new LinkedMultiValueMap<>();

        if (!userRepository.existsByNickname(userDto.getSerialCode())) {
            throw new UnAuthorizedException("식별코드가 일치하지 않습니다.", ACCESS_DENIED_EXCEPTION);
        } else if (userRepository.existsByNickname(userDto.getNickname())) {
            throw new UnAuthorizedException("중복된 닉네임입니다.", ACCESS_DENIED_EXCEPTION);
        }

        User user = userRepository.findByNickname(userDto.getSerialCode());
        user.update(userDto);

        user = userRepository.findByNickname(userDto.getNickname());
        httpSession.setAttribute("user", new UserResponseDto(user));
        sessionCarrier.add("session", httpSession.getAttribute("user"));
        sessionCarrier.add("message", "회원가입에 성공했습니다.");

        return sessionCarrier;
    }

    @Transactional
    public MultiValueMap<String, Object> checkUser(String code) {
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
                User userDto = User.builder()
                        .nickname(user.getNickname())
                        .email(email)
                        .introduction(user.getIntroduction())
                        .userRole(user.getUserRole())
                        .build();

                httpSession.setAttribute("user", new UserResponseDto(userDto));
                sessionCarrier.add("session", httpSession.getAttribute("user"));
                sessionCarrier.add("message", "이미 가입한 회원입니다.");
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

            User user = userRepository.save(userDto);
            sessionCarrier.add("SerialCode", nickname);
            sessionCarrier.add("message", "처음 방문한 회원입니다.");
        }
        return sessionCarrier;
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
