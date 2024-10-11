package com.school.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.school.config.JwtUtil;
import com.school.school.config.SchoolConfig;
import com.school.school.config.SecurityConfig;
import com.school.school.dto.User;
import com.school.school.service.UserService;
import com.school.school.utils.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(controllers = UserController.class)
@Import({SecurityConfig.class})
public class UserControllerTest {

    private static final String API_USERS = "/api/v1/users";


    @MockBean
    private UserService userService;

    @MockBean
    JwtUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "admin@yopmail.com", authorities = {"Admin"})
    public void testGetAllUsers() throws Exception {

        List<User> users = List.of(TestData.buildUser());

        when(userService.findAll()).thenReturn(users);

        MvcResult mvcResult = mockMvc.perform(get(API_USERS).accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).isNotNull();
        verify(userService).findAll();

    }

    @Test
    @WithAnonymousUser
    public void testGetAllUsersShouldReturnForbidden() throws Exception {

        List<User> users = List.of(TestData.buildUser());

        when(userService.findAll()).thenReturn(users);

        mockMvc.perform(get(API_USERS).accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();

        verify(userService, never()).findAll();

    }


    @Test
    @WithMockUser(username = "admin@yopmail.com", authorities = {"Admin"})
    public void testCreateUser() throws Exception {
        // Create an ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        User user = TestData.buildUser();

        when(userService.createUser(user)).thenReturn(user);

        mockMvc.perform(post(API_USERS + "/register").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andReturn();

        verify(userService, never()).findAll();

    }


}
