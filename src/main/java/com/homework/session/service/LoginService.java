package com.homework.session.service;

import com.homework.session.Repository.UserRepository;
import com.homework.session.config.LoginUser;
import com.homework.session.dto.UserDto;
import com.homework.session.entity.User;
import com.homework.session.error.exception.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.homework.session.error.ErrorCode.ACCESS_DENIED_EXCEPTION;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class LoginService {

    private final UserRepository userRepository;

    @Transactional
    public String signUp(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getNickname())) {
            throw new UnAuthorizedException("중복된 닉네임입니다.", ACCESS_DENIED_EXCEPTION);
        }

        User user = User.builder()
                .email(userDto.getEmail())
                .nickname(userDto.getNickname())
                .picture(userDto.getPicture())
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
    public UserDto myPage(UserDto userDto, @LoginUser UserDto loginUser) {
        if (loginUser == null) {
            throw new UnAuthorizedException("로그인이 필요합니다.", ACCESS_DENIED_EXCEPTION);
        }

        UserDto myDto = UserDto.builder()
                .nickname(userDto.getNickname())
                .userRole(userDto.getUserRole())
                .introduction(userDto.getIntroduction())
                .build();

        return userRepository.findByNicknameAndUserRoleAndIntroduction(myDto.getNickname(), myDto.getUserRole(),
                myDto.getIntroduction());
    }

    @Transactional
    public void update(UserDto userDto, @LoginUser UserDto loginUser) {

        User user = userRepository.findByEmail(userDto.getEmail()).orElseThrow(() ->
        { throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION); });

        UserDto updateDto = UserDto.builder()
                .nickname(userDto.getNickname())
                .picture(userDto.getPicture())
                .introduction(userDto.getIntroduction())
                .build();

        if ( loginUser != null ) {
            user.update(updateDto);
        }
    }

    @Transactional
    public void delete(UserDto userDto, @LoginUser UserDto loginUser) {
        User user = userRepository.findByEmail(userDto.getEmail()).orElseThrow(() ->
            { throw new UnAuthorizedException("E0002", ACCESS_DENIED_EXCEPTION); });

        if ( loginUser != null ) {
            userRepository.delete(user);
        }
    }
}
