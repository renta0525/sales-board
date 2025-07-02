package com.example.sales_board.repository;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.sales_board.domain.User;

@Repository
public class UserRepository {
    
    @Autowired
    private NamedParameterJdbcTemplate template;

    private static final RowMapper<User> USE_ROW_MAPPER = (rs, i) -> {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setCreatedAt(rs.getTimestamp( "created_at"));
        return user;
    };

    /**
     * emailアドレスで検索
     * @param email
     * @return
     */
    public Optional<User> findByEmail (String email) {
        String sql = "SELECT * FROM users WHERE email = :email";
        SqlParameterSource param = new MapSqlParameterSource().addValue("email", email);
        try {
            User user = template.queryForObject(sql, param, USE_ROW_MAPPER);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
