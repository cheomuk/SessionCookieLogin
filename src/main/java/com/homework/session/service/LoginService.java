package com.homework.session.service;

import com.homework.session.Repository.UserRepository;
import com.homework.session.config.LoginUser;
import com.homework.session.dto.UserDto.UserRequestDto;
import com.homework.session.entity.User;
import com.homework.session.error.exception.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.homework.session.error.ErrorCode.ACCESS_DENIED_EXCEPTION;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class LoginService {

    private final UserRepository userRepository;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Transactional
    public String signUp(UserRequestDto userDto, OAuth2AccessToken tokenRequest) {
        if (userRepository.existsByNickname(userDto.getNickname())) {
            throw new UnAuthorizedException("중복된 닉네임입니다.", ACCESS_DENIED_EXCEPTION);
        }

        String email = customOAuth2UserService.findKakaoUser(tokenRequest);

        User user = User.builder()
                .nickname(userDto.getNickname())
                .email(email)
                .introduction(userDto.getIntroduction())
                .userRole(userDto.getUserRole())
                .build();

        userRepository.save(user);
        return "/";
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

        String email = loginUser.getEmail();

        UserRequestDto myDto = UserRequestDto.builder()
                .nickname(userDto.getNickname())
                .email(email)
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
