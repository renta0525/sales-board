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

import com.example.sales_board.dto.DailySalesDTO;

@JdbcTest
@Import(DailyRepository.class)
@Sql("file:db/DB_table_create.sql")
@DisplayName("DailyRepository のテスト")
public class DailyRepositoryTest {

    @Autowired
    private DailyRepository dailyRepository;

    @Autowired
    private NamedParameterJdbcTemplate template;

    @BeforeEach
    void setUp() {
        // テストデータ挿入
        String insertSql = """
            INSERT INTO users (id, name, email, password) VALUES
            (1, 'テストユーザー', 'test@example.com', 'password');

            INSERT INTO categories (id, name) VALUES (1, 'テストカテゴリ');
            INSERT INTO items (id, name, price, category_id) VALUES (1, 'テスト商品', 1000, 1);

            INSERT INTO orders (id, user_id, order_date, total_price) VALUES
            (1, 1, '2024-07-01 10:00:00', 1000),
            (2, 1, '2024-07-01 14:00:00', 2000), -- 同日複数回注文
            (3, 1, '2024-07-02 11:00:00', 3000),
            (4, 1, '2024-07-04 18:00:00', 4000),
            (5, 1, '2024-08-01 09:00:00', 5000); -- 期間外
        """;
        template.getJdbcOperations().execute(insertSql);
    }

    @Test
    @DisplayName("指定期間内の日別売上が正しく集計され、日付順にソートされていること")
    void findDailySales_success() {
        LocalDate from = LocalDate.of(2024, 7, 1);
        LocalDate to = LocalDate.of(2024, 7, 31);

        List<DailySalesDTO> result = dailyRepository.findDailySales(from, to);

        assertEquals(3, result.size(), "3日分の売上データが返されるはずです");

        // 2024-07-01
        assertEquals(LocalDate.of(2024, 7, 1), result.get(0).getDate());
        assertEquals(3000, result.get(0).getTotalSales()); // 1000 + 2000

        // 2024-07-02
        assertEquals(LocalDate.of(2024, 7, 2), result.get(1).getDate());
        assertEquals(3000, result.get(1).getTotalSales());

        // 2024-07-04
        assertEquals(LocalDate.of(2024, 7, 4), result.get(2).getDate());
        assertEquals(4000, result.get(2).getTotalSales());
    }

    @Test
    @DisplayName("データが存在しない期間を指定した場合、空のリストが返されること")
    void findDailySales_noData() {
        LocalDate from = LocalDate.of(2025, 1, 1);
        LocalDate to = LocalDate.of(2025, 1, 31);

        List<DailySalesDTO> result = dailyRepository.findDailySales(from, to);

        assertTrue(result.isEmpty(), "データがないので結果は空のはずです");
    }
}
