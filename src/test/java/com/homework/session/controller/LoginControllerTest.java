package com.homework.session.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.session.Repository.UserRepository;
import com.homework.session.dto.UserDto;
import com.homework.session.entity.User;
import com.homework.session.error.exception.UnauthorizedException;
import com.homework.session.service.LoginService;
import com.homework.session.sessionManager.SessionManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.homework.session.error.ErrorCode.ACCESS_DENIED_EXCEPTION;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginService loginService;

    @Autowired
    private SessionManager sessionManager;

    public UserDto UserDto() {
        return UserDto.builder()
                .email("email1@email.com")
                .password("password")
                .phoneNumber("01012345675")
                .build();
    }

    @Test
    public void login_Test() throws Exception {
        UserDto userDto = UserDto();

        User user = userRepository.findByEmail(userDto.getEmail())
                .filter(u -> u.getPassword().equals(userDto.getPassword()))
                .orElseThrow(() -> new UnauthorizedException("E0002", ACCESS_DENIED_EXCEPTION) );

        MockHttpServletResponse response = new MockHttpServletResponse();
        sessionManager.createSession(userDto, response);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                        .andExpect(status().isOk());
    }

    @Test
    public void logout_Test() throws Exception {
        UserDto userDto = UserDto();

        MockHttpServletResponse response = new MockHttpServletResponse();
        sessionManager.createSession(userDto, response);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());

        sessionManager.expire(request, response);
        Object expiredSession = sessionManager.getSession(request);
        assertThat(expiredSession).isNull();
    }

    @Test
    public void signUp_Test() throws Exception {
        UserDto userDto = UserDto();

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                        .andExpect(status().isCreated());

    }

    @Test
    public void update_Test() throws Exception {
        UserDto userDto = UserDto();

        User user = userRepository.findByEmail(userDto.getEmail()).orElseThrow(() ->
                        { throw new UnauthorizedException("E0002", ACCESS_DENIED_EXCEPTION); });

        user.update(userDto);

        mockMvc.perform(put("/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                        .andExpect(status().isOk());
    }

    @Test
    public void delete_Test() throws Exception {
        UserDto userDto = UserDto();

        User user = User.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .phoneNumber(userDto.getPhoneNumber())
                .build();

        userRepository.save(user);

        mockMvc.perform(delete("/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                        .andExpect(status().isOk());

        assertThat(userRepository.findByEmail("email1@email.com").isEmpty());
    }
}
