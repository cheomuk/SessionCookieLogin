package com.homework.session.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.session.Repository.UserRepository;
import com.homework.session.dto.UserDto;
import com.homework.session.sessionManager.SessionManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

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
    private UserRepository userRepository;

    @Autowired
    private SessionManager sessionManager;

    @Test
    public void serviceLogin_Test() throws Exception {

    }

    @Test
    public void serviceSignup_Test() throws Exception {

    }

    @Test
    public void serviceUpdate_Test() throws Exception {

    }

    @Test
    public void serviceDelete_Test() throws Exception {

    }
}
