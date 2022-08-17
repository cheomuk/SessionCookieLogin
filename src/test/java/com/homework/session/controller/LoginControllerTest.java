//package com.homework.session.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.homework.session.Repository.UserRepository;
//import com.homework.session.dto.UserDto.UserRequestDto;
//import com.homework.session.service.LoginService;
//import com.homework.session.sessionManager.SessionManager;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
//@Transactional
//public class LoginControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private LoginService loginService;
//
//    @Autowired
//    private SessionManager sessionManager;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    public UserRequestDto UserDtoTest() {
//        return UserRequestDto.builder()
//                .email("testgmail@gmail.com")
//                .nickname("테스트 유저1")
//                .password("1234")
//                .phoneNumber("01012345679")
//                .build();
//    }
//
//    @Test
//    public void signUp_Test() throws Exception {
//        UserRequestDto userDto = UserDtoTest();
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(userDto)))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void login_Test() throws Exception {
//        UserRequestDto userDto = UserRequestDto.builder()
//                            .email("gmail@email.com")
//                            .password("1234")
//                            .build();
//
//        mockMvc.perform(post("/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(userDto)))
//                        .andExpect(status().isOk());
//    }
//
//    @Test
//    public void update_Test() throws Exception {
//        UserRequestDto userDto = UserDtoTest();
//
//        mockMvc.perform(put("/update")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(userDto)))
//                        .andExpect(status().isOk());
//    }
//
//    @Test
//    public void delete_Test() throws Exception {
//        UserRequestDto userDto = UserDtoTest();
//
//        mockMvc.perform(delete("/delete")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(userDto)))
//                        .andExpect(status().isOk());
//
//        assertThat(userRepository.findByEmail(UserDtoTest().getEmail()).isEmpty());
//    }
//
//    @Test
//    public void logout_Test() throws Exception {
//        UserRequestDto userDto = UserDtoTest();
//
//        MockHttpServletResponse response = new MockHttpServletResponse();
//        sessionManager.createSession(userDto, response);
//
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        request.setCookies(response.getCookies());
//
//        sessionManager.expire(request);
//        Object expiredSession = sessionManager.getSession(request);
//        assertThat(expiredSession).isNull();
//    }
//}
