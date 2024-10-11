package com.school.school.repository;

import com.school.school.dto.Classroom;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class ClassroomRepository {

    private final JdbcTemplate jdbcTemplate;


    public ClassroomRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Classroom> fillClassroom = (rs, rowNum) -> new Classroom(rs.getLong("id"), rs.getString("name"));


    public List<Classroom> findAll() {
        return jdbcTemplate.query("SELECT * FROM school.user", fillClassroom);
    }

    public Classroom findById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM school.user where id =?", fillClassroom, id);
    }

    public Classroom save(Classroom classroom) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO school.classroom (name) VALUES (?)", new String[]{"id"});
            ps.setString(1, classroom.name());
            return ps;
        }, keyHolder);

        int generatedId = Optional.of(keyHolder).map(e -> Objects.requireNonNull(e.getKey()).intValue()).orElse(0);

        return new Classroom((long) generatedId, classroom.name());
    }
}
