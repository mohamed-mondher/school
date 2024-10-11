package com.school.school.repository;

import com.school.school.dto.Role;
import com.school.school.dto.User;
import com.school.school.utils.Exception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserRepository {

    private JdbcTemplate jdbcTemplate;


    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UserRepository() {

    }

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> new User(rs.getLong("id"), rs.getString("firstname"), rs.getString("lastname"), rs.getString("email"), rs.getString("password"), Role.valueOf(rs.getString("role")));

    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM school.user", userRowMapper);
    }

    public User findById(Long id) {
        User user = jdbcTemplate.queryForObject("SELECT * FROM school.user WHERE id = ?", userRowMapper, id);
        return user;
    }

    public User save(User user) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO school.user (firstname, lastname, email, role, password) VALUES (?, ?, ?, ?, ?)",
                    new String[]{"id"});
            ps.setString(1, user.firstname());
            ps.setString(2, user.lastname());
            ps.setString(3, user.email());
            ps.setObject(4, user.role().name());
            ps.setString(5, user.password());
            return ps;
        }, keyHolder);

        int generatedId = Optional.of(keyHolder).map(e -> Objects.requireNonNull(e.getKey()).intValue()).orElse(0);

        return new User((long) generatedId, user.firstname(), user.lastname(), user.email(), user.password(), user.role());

    }

    public int update(User user, Long id) {
        int result = jdbcTemplate.update("UPDATE school.user SET firstname = ?,lastname=?, email = ?,role=?,password=? WHERE id = ?", user.firstname(), user.lastname(), user.email(), user.role(), user.password(), id);
        return result;
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM school.user WHERE id = ?", id);
    }

    public User findByEmail(String email) {
        return jdbcTemplate.queryForObject("SELECT * FROM school.user WHERE email = ? ", userRowMapper, email);
    }


}
