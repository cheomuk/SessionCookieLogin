package com.homework.session.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.session.Repository.UserRepository;
import com.homework.session.dto.UserDto;
import com.homework.session.entity.User;
import com.homework.session.error.exception.UnauthorizedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.homework.session.error.ErrorCode.ACCESS_DENIED_EXCEPTION;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class LoginServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginService loginService;

    public UserDto UserDtoTest() {
        return UserDto.builder()
                .email("testemail1@email.com")
                .password("1234")
                .phoneNumber("01012345679")
                .build();
    }

    @Test
    public void serviceLogin_Test() throws Exception {
        UserDto userDto = UserDtoTest();

        loginService.login(userDto.getEmail(), userDto.getPassword());
    }

    @Test
    public void serviceSignup_Test() throws Exception {
        UserDto userDto = UserDtoTest();

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new UnauthorizedException("E0002", ACCESS_DENIED_EXCEPTION);
        }

        User user = User.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .phoneNumber(userDto.getPhoneNumber())
                .build();

        userRepository.save(user);
    }

    @Test
    public void serviceUpdate_Test() throws Exception {
        UserDto userDto = UserDtoTest();

        User user = userRepository.findByEmail(userDto.getEmail()).orElseThrow(() ->
            { throw new UnauthorizedException("E0002", ACCESS_DENIED_EXCEPTION); });

        UserDto updateDto = UserDto.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .phoneNumber(userDto.getPhoneNumber())
                .build();

        user.update(updateDto);
    }

    @Test
    public void serviceDelete_Test() throws Exception {
        UserDto userDto = UserDtoTest();

        User user = userRepository.findByEmail(userDto.getEmail()).orElseThrow(() ->
            { throw new UnauthorizedException("E0002", ACCESS_DENIED_EXCEPTION); });

        userRepository.delete(user);
    }
}
