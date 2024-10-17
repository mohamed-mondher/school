package com.school.school.repository;

import com.school.school.dto.Classroom;
import com.school.school.dto.Role;
import com.school.school.dto.Student;
import com.school.school.dto.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class StudentRepository {

    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final ClassroomRepository classroomRepository;

    public StudentRepository(JdbcTemplate jdbcTemplate, UserRepository userRepository, PasswordEncoder passwordEncoder, ClassroomRepository classroomRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.classroomRepository = classroomRepository;
    }

    // Mapper pour convertir une ligne en record User
    private final RowMapper<Student> fullUserRowMapper = (rs, rowNum) -> new Student(rs.getLong("id"), new Classroom(                                // Objet Classroom
            rs.getLong("id"), rs.getString("name")), new User(rs.getLong("id"),
            rs.getString("firstname"), rs.getString("lastname"),
            rs.getString("email"), rs.getString("password"), Role.valueOf(rs.getString("role"))));


    public List<Student> findAll() {

        String sql = """
                    SELECT s.id , cl.id , cl.name, u.id,u.firstname,u.lastname,u.email,u.password,u.role 
                        FROM school.student s
                        inner join school.classroom cl on s.classroom_id = cl.id
                        inner join school.user u on u.id= s.user_id
                """;

        return jdbcTemplate.query(sql, fullUserRowMapper);
    }

    public Student findById(Long id) {

        String sql = """
                    SELECT s.id , cl.id , cl.name, u.id,u.firstname,u.lastname,u.email,u.password,u.role 
                        FROM school.student s
                        inner join school.classroom cl on s.classroom_id = cl.id
                        inner join school.user u on u.id= s.user_id
                        where s.id = ?
                """;


        return jdbcTemplate.queryForObject(sql, fullUserRowMapper, id);
    }

    public Student save(Student student) {
        userRepository.findByEmail(student.user().email());
        String encodedPassword = passwordEncoder.encode(student.user().password());  // Encode the password
        User newUser = new User(null, student.user().firstname(), student.user().lastname(), student.user().email(), encodedPassword, student.user().role());
        User savedUser = userRepository.save(newUser);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO school.student (classroom_id,user_id) VALUES (?, ?)", new String[]{"id"});
            ps.setObject(1, null);
            ps.setObject(2, savedUser.id());
            return ps;
        }, keyHolder);
        int generatedId = Optional.of(keyHolder).map(e -> Objects.requireNonNull(e.getKey()).intValue()).orElse(0);

        return new Student((long) generatedId, null, savedUser);
    }

    public Student assignUserToClassroom(Long id, Long classroomId) {

        jdbcTemplate.update("UPDATE school.student SET classroom_id = ? WHERE id = ?", classroomId, id);

        return findById(id);
    }

}
