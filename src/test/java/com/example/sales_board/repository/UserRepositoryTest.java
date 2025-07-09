package com.example.sales_board.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import com.example.sales_board.domain.User;

@JdbcTest
@Import(UserRepository.class)
@Sql("file:db/DB_table_create.sql")
@DisplayName("UserRepository のテスト")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NamedParameterJdbcTemplate template;

    @BeforeEach
    void setUp() {
        // テストデータの準備
        String insertSql = "INSERT INTO users (id, name, email, password, created_at) VALUES (:id, :name, :email, :password, :createdAt)";
        
        MapSqlParameterSource param = new MapSqlParameterSource()
            .addValue("id", 1)
            .addValue("name", "テストユーザー")
            .addValue("email", "test@example.com")
            .addValue("password", "password")
            .addValue("createdAt", Timestamp.valueOf(LocalDateTime.now()));
        
        // @Sqlアノテーションで各テストの前にテーブルが再作成されるため、データの削除は不要
        template.update(insertSql, param);
    }

    @Test
    @DisplayName("存在するメールアドレスで検索した場合、ユーザーが返されること")
    void findByEmail_existingUser() {
        // 実行
        Optional<User> result = userRepository.findByEmail("test@example.com");

        // 検証
        assertTrue(result.isPresent(), "ユーザーが見つかるはずです");
        User user = result.get();
        assertEquals("テストユーザー", user.getName());
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    @DisplayName("存在しないメールアドレスで検索した場合、空のOptionalが返されること")
    void findByEmail_nonExistingUser() {
        // 実行
        Optional<User> result = userRepository.findByEmail("non-existent@example.com");

        // 検証
        assertFalse(result.isPresent(), "ユーザーは見つからないはずです");
    }
}