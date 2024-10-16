package com.school.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.school.config.SchoolContainerConfig;
import com.school.school.config.SecurityConfig;
import com.school.school.dto.User;
import com.school.school.utils.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
@Import({SecurityConfig.class, SchoolContainerConfig.class})
public class UserControllerIT {

    private static final String API_USERS = "/api/v1/users";


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
    }

    @Test
    @WithMockUser(username = "admin@yopmail.com", authorities = {"Admin"})
    public void getAllUsersShouldReturnOk() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get(API_USERS).accept(org.springframework.http.MediaType.APPLICATION_JSON)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).isNotNull();
    }

    @Test
    @WithAnonymousUser
    public void getAllUsersShouldReturnForbidden() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get(API_USERS).accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        assertThat(responseContent).isNotNull().isNotEmpty();
    }

    @Test
    @WithMockUser(username = "admin@yopmail.com", authorities = {"Admin"})
    public void getUserByIdShouldReturnOk() throws Exception {

        Long userId = 1L;
        MvcResult mvcResult = mockMvc.perform(get(API_USERS + "/{id}", userId).accept(org.springframework.http.MediaType.APPLICATION_JSON)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();


        String responseContent = mvcResult.getResponse().getContentAsString();
        assertThat(responseContent).isNotNull();
        User jsonResponse = objectMapper.readValue(responseContent, User.class);
        assertThat(jsonResponse).isNotNull();
        assertThat(jsonResponse.id()).isNotNull();
        assertThat(jsonResponse.id()).isEqualTo(userId);
    }


    @Test
    @WithMockUser(username = "admin@yopmail.com", authorities = {"Admin"})
    public void getUserByIdShouldReturnNotFound() throws Exception {

        Long userId = 998L;
        MvcResult mvcResult = mockMvc.perform(get(API_USERS + "/{id}", userId).accept(org.springframework.http.MediaType.APPLICATION_JSON)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        assertThat(responseContent).isNotNull().isEqualTo("User with id " + 999L + " not found");
    }

    @Test
    @WithMockUser(username = "admin@yopmail.com", authorities = {"Admin"})
    public void createUserShouldReturnCreatedUser() throws Exception {
        // Create an ObjectMapper instance
        User user = TestData.buildUser();

        MvcResult mvcResult = mockMvc.perform(post(API_USERS + "/register").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        assertThat(responseContent).isNotNull();
        User jsonResponse = objectMapper.readValue(responseContent, User.class);
        assertThat(jsonResponse).isNotNull();
        assertThat(jsonResponse.id()).isNotNull();
    }

    @Test
    @WithMockUser(username = "admin@yopmail.com", authorities = {"Admin"})
    public void createUserShouldConfilct() throws Exception {
        // Create an ObjectMapper instance
        User user = TestData.buildUser();

        MvcResult mvcResult = mockMvc.perform(post(API_USERS + "/register").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isConflict())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        assertThat(responseContent).isNotNull();
    }


    @Test
    @WithMockUser(username = "admin@yopmail.com", authorities = {"Admin"})
    public void deleteUser() throws Exception {
        Long userId = 1004L;

        mockMvc.perform(delete(API_USERS + "/{id}", userId).accept(org.springframework.http.MediaType.APPLICATION_JSON)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

}
