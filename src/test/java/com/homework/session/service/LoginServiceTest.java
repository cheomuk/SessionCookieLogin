package com.homework.session.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.session.Repository.UserRepository;
import com.homework.session.dto.UserDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertNotEquals;

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

//    public UserDto UserDtoTest() {
//        return UserDto.builder()
//                .email("testgmail@gmail.com")
//                .nickname("테스트 유저1")
//                .password(passwordEncoder.encode("1234"))
//                .phoneNumber("01012345679")
//                .build();
//    }
//
//    @Test
//    public void serviceSignUp_Test() throws Exception {
//        UserDto userDto = UserDtoTest();
//
//        if (userRepository.findByEmail(userDto.getEmail()) == null) {
//            loginService.signUp(userDto);
//        }
//
//        UserDto testDto = UserDto.builder()
//                .email("testemail5@email.com")
//                .nickname("테스트 유저5")
//                .password("1234")
//                .phoneNumber("01099998888")
//                .build();
//
//        assertNotEquals(userDto.getEmail(), testDto.getEmail());
//        assertNotEquals(userDto.getNickname(), testDto.getNickname());
//        loginService.signUp(testDto);
//    }
//
//    @Test
//    public void serviceLogin_Test() throws Exception {
//        UserDto userDto = UserDtoTest();
//
//        if (userRepository.findByEmail(userDto.getEmail()) == null) {
//            loginService.signUp(userDto);
//        }
//
//        UserDto testDto = UserDto.builder()
//                .email("testgmail@gmail.com")
//                .password("1234")
//                .build();
//
//        MockHttpServletRequest request = new MockHttpServletRequest();
//
//        assertThat(userDto.getEmail().equals(testDto.getEmail()));
//        assertThat(passwordEncoder.matches(testDto.getPassword(), userDto.getPassword()));
//
//        loginService.login(testDto.getEmail(), testDto.getPassword(), request);
//    }
//
//    @Test
//    public void serviceUpdate_Test() throws Exception {
//        UserDto userDto = UserDtoTest();
//
//        if (userRepository.findByEmail(userDto.getEmail()) == null) {
//            loginService.signUp(userDto);
//        }
//
//        UserDto testDto = UserDto.builder()
//                .email("testgmail@gmail.com")
//                .nickname("테스트 유저1")
//                .password("1234")
//                .phoneNumber("01012344321")
//                .build();
//
//        MockHttpServletRequest request = new MockHttpServletRequest();
//
//        assertThat(userDto.getEmail()).isEqualTo(testDto.getEmail());
//        loginService.update(testDto, request);
//    }
//
//    @Test
//    public void serviceDelete_Test() throws Exception {
//        UserDto userDto = UserDtoTest();
//
//        if (userRepository.findByEmail(userDto.getEmail()) == null) {
//            loginService.signUp(userDto);
//        }
//
//        UserDto testDto = UserDto.builder()
//                .email("testgmail@gmail.com")
//                .build();
//
//        MockHttpServletRequest request = new MockHttpServletRequest();
//
//        assertThat(userDto.getEmail()).isEqualTo(testDto.getEmail());
//        loginService.delete(testDto, );
//    }
}
