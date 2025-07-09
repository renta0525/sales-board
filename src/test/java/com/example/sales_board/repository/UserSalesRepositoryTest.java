package com.example.sales_board.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import com.example.sales_board.dto.UserSalesDTO;

@JdbcTest
@Import(UserSalesRepository.class)
@Sql("file:db/DB_table_create.sql")
@DisplayName("UserSalesRepository のテスト")
public class UserSalesRepositoryTest {

    @Autowired
    private UserSalesRepository userSalesRepository;

    @Autowired
    private NamedParameterJdbcTemplate template;

    @BeforeEach
    void setUp() {
        // テストデータ挿入
        String insertSql = """
            INSERT INTO users (id, name, email, password) VALUES
            (1, '山田 太郎', 'taro@example.com', 'password'),
            (2, '鈴木 花子', 'hanako@example.com', 'password'),
            (3, '佐藤 次郎', 'jiro@example.com', 'password');

            INSERT INTO orders (id, user_id, order_date, total_price) VALUES
            (1, 1, '2024-07-05 10:00:00', 3000), -- 山田
            (2, 2, '2024-07-10 11:00:00', 5000), -- 鈴木
            (3, 1, '2024-07-15 14:00:00', 2000), -- 山田
            (4, 3, '2024-07-20 18:00:00', 8000), -- 佐藤
            (5, 2, '2024-08-01 09:00:00', 1000); -- 鈴木 (期間外)
        """;
        template.getJdbcOperations().execute(insertSql);
    }

    @Test
    @DisplayName("指定期間内のユーザー別売上が正しく集計され、売上順にソートされていること")
    void findUserSales_success() {
        LocalDate from = LocalDate.of(2024, 7, 1);
        LocalDate to = LocalDate.of(2024, 7, 31);

        List<UserSalesDTO> result = userSalesRepository.findUserSales(from, to);

        assertEquals(3, result.size(), "3人分の売上データが返されるはずです");

        // 1位: 佐藤 次郎
        assertEquals("佐藤 次郎", result.get(0).getUserName());
        assertEquals(8000, result.get(0).getTotalSales());

        // 2位: 山田 太郎
        assertEquals("山田 太郎", result.get(1).getUserName());
        assertEquals(5000, result.get(1).getTotalSales()); // 3000 + 2000

        // 3位: 鈴木 花子
        assertEquals("鈴木 花子", result.get(2).getUserName());
        assertEquals(5000, result.get(2).getTotalSales());
    }

    @Test
    @DisplayName("データが存在しない期間を指定した場合、空のリストが返されること")
    void findUserSales_noData() {
        LocalDate from = LocalDate.of(2025, 1, 1);
        LocalDate to = LocalDate.of(2025, 1, 31);

        List<UserSalesDTO> result = userSalesRepository.findUserSales(from, to);

        assertTrue(result.isEmpty(), "データがないので結果は空のはずです");
    }
}
