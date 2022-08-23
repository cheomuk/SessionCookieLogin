package com.homework.session.service;

import com.homework.session.Repository.UserRepository;
import com.homework.session.config.LoginUser;
import com.homework.session.dto.UserDto.UserRequestDto;
import com.homework.session.dto.UserDto.UserResponseDto;
import com.homework.session.entity.User;
import com.homework.session.error.exception.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

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
    public MultiValueMap<String, Object> signUp(UserRequestDto userDto, HttpServletRequest request) {

        MultiValueMap<String, Object> sessionCarrier = new LinkedMultiValueMap<>();

        if (userRepository.existsByEmail(userDto.getEmail()) || userDto.getEmail() == null) {
            throw new UnAuthorizedException("잘못된 접근입니다.", ACCESS_DENIED_EXCEPTION);
        } else if (userRepository.existsByNickname(userDto.getNickname())) {
            throw new UnAuthorizedException("중복된 닉네임입니다.", ACCESS_DENIED_EXCEPTION);
        }

        User user = User.builder()
                .nickname(userDto.getNickname())
                .email(userDto.getEmail())
                .introduction(userDto.getIntroduction())
                .userRole(userDto.getUserRole())
                .build();

        httpSession.setAttribute("user", new UserResponseDto(user));
        sessionCarrier.add("session", httpSession.getAttribute("user"));
        sessionCarrier.add("message", "회원가입에 성공했습니다.");

        userRepository.save(user);
        return sessionCarrier;
    }

    @Transactional
    public MultiValueMap<String, Object> checkUser(String code, HttpServletRequest request) {
        String access_token = kakaoAPI.getAccessToken(code);
        HashMap<String, Object> userInfo = kakaoAPI.getUserInfo(access_token);
        MultiValueMap<String, Object> sessionCarrier = new LinkedMultiValueMap<>();
        String email = userInfo.get("email").toString();

        if (userRepository.existsByEmail(email)) {

            User user = userRepository.findByEmail(email).orElseThrow(() ->
                { throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION); });

            User userDto = User.builder()
                    .nickname(user.getNickname())
                    .email(user.getEmail())
                    .introduction(user.getIntroduction())
                    .userRole(user.getUserRole())
                    .build();

            httpSession.setAttribute("user", new UserResponseDto(userDto));
            sessionCarrier.add("session", httpSession.getAttribute("user"));
            sessionCarrier.add("message", "이미 가입한 회원입니다.");
            return sessionCarrier;
        } else {
            sessionCarrier.add("email", email);
            sessionCarrier.add("message", "처음 방문한 회원입니다.");
            return sessionCarrier;
        }
    }

    @Transactional
    public boolean checkNickname(String nickname) {
        boolean nicknameDuplicate = userRepository.existsByNickname(nickname);
        return !nicknameDuplicate;
    }

    @Transactional
    public UserRequestDto myPage(UserRequestDto userDto, @LoginUser UserRequestDto loginUser) {
        if (loginUser == null) {
            throw new UnAuthorizedException("로그인이 필요합니다.", ACCESS_DENIED_EXCEPTION);
        }

        UserRequestDto myDto = UserRequestDto.builder()
                .nickname(userDto.getNickname())
                .userRole(userDto.getUserRole())
                .introduction(userDto.getIntroduction())
                .build();

        return userRepository.findByNicknameAndUserRoleAndIntroduction(myDto.getNickname(), myDto.getUserRole(),
                myDto.getIntroduction());
    }

    @Transactional
    public void delete(String nickname, @LoginUser UserRequestDto loginUser) {
        User user = userRepository.findByEmail(nickname).orElseThrow(() ->
            { throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION); });

        if ( loginUser != null ) {
            userRepository.delete(user);
        }
    }
}
