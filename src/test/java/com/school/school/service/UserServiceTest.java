package com.school.school.service;

import com.school.school.dto.Role;
import com.school.school.dto.User;
import com.school.school.repository.UserRepository;
import com.school.school.utils.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith({SpringExtension.class})
@ActiveProfiles("test")
class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @Mock
    private JdbcTemplate jdbcTemplate;


    private final RowMapper<User> userRowMapper = mock(RowMapper.class);

    @Test
    void findAll_should_return_all_users() {
        doReturn(List.of(TestData.buildUser())).when(userRepository).findAll();
        List<User> result = userService.findAll();
        assertThat(result).isNotNull().isNotEmpty();
        assertThat(result).isNotNull().hasSize(1);
        assertThat(result.get(0).password()).contains("pwd");

    }

    @Test
    void findById_should_return_user_by_id() {
        doReturn(TestData.buildUser()).when(userRepository).findById(1L);
        User result = userService.findUserById(1L);
        assertThat(result).isNotNull();
        assertThat(result.password()).contains("pwd");

    }

    @Test
    void findById_should_return_user_not_found() {
        // When
        when(userRepository.findById(999L)).thenReturn(null);

        // Assert

        Exception exception = assertThrows(Exception.class, () -> userService.findUserById(999L));
        assertEquals("user not found" + 999L, exception.getMessage());

    }

    @Test
    void save_shouldInsertUserAndReturnIt() throws SQLException {
        User user = TestData.buildUser();

        doReturn(user).when(userRepository).save(user);


        User savedUser = userRepository.save(user);

        assertNotNull(savedUser);
        assertEquals(1L, savedUser.id());
        assertEquals("test firstname", savedUser.firstname());
        assertEquals("test lastname", savedUser.lastname());
        assertEquals("test-email@yopmail.com", savedUser.email());
        assertEquals("pwd", savedUser.password());
        assertEquals(Role.Student, savedUser.role());
    }
}





