package com.homework.session.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.session.Repository.UserRepository;
import com.homework.session.dto.UserDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
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

    public UserDto UserDtoTest() {
        return UserDto.builder()
                .email("testemail1@email.com")
                .nickName("테스트 유저2")
                .password(passwordEncoder.encode("1234"))
                .phoneNumber("01012345679")
                .build();
    }

    @Test
    public void serviceLogin_Test() throws Exception {
        UserDto userDto = UserDtoTest();

        UserDto testDto = UserDto.builder()
                .email("testemail1@email.com")
                .nickName("테스트 유저2")
                .password("1234")
                .build();

        MockHttpServletRequest request = new MockHttpServletRequest();

        assertThat(userDto.getEmail().equals(testDto.getEmail()));
        assertThat(passwordEncoder.matches(userDto.getPassword(), testDto.getPassword()));

        loginService.login(testDto.getEmail(), testDto.getPassword(), request);
    }

    @Test
    public void serviceSignUp_Test() throws Exception {
        UserDto userDto = UserDtoTest();

        UserDto testDto = UserDto.builder()
                .email("testemail6@email.com")
                .nickName("테스트 유저2")
                .password("1234")
                .phoneNumber("01099998888")
                .build();

        assertNotEquals(userDto.getEmail(), testDto.getEmail());
        loginService.signUp(testDto);
    }

    @Test
    public void serviceUpdate_Test() throws Exception {
        UserDto userDto = UserDtoTest();

        UserDto testDto = UserDto.builder()
                .email("testemail1@email.com")
                .nickName("테스트 유저2")
                .password("1234")
                .phoneNumber("01012344321")
                .build();

        MockHttpServletRequest request = new MockHttpServletRequest();

        assertThat(userDto.getEmail()).isEqualTo(testDto.getEmail());
        loginService.update(testDto, request);
    }

    @Test
    public void serviceDelete_Test() throws Exception {
        UserDto userDto = UserDtoTest();

        UserDto testDto = UserDto.builder()
                .email("testemail1@email.com")
                .build();

        MockHttpServletRequest request = new MockHttpServletRequest();

        assertThat(userDto.getEmail()).isEqualTo(testDto.getEmail());
        loginService.delete(testDto, request);
    }

    @After
    public void TestEnd() {
        userRepository.deleteAll();
    }
}
