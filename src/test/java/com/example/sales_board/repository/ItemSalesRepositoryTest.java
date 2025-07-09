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

import com.example.sales_board.dto.ItemSalesDTO;

@JdbcTest
@Import(ItemSalesRepository.class)
@Sql("file:db/DB_table_create.sql")
@DisplayName("ItemSalesRepository のテスト")
public class ItemSalesRepositoryTest {

    @Autowired
    private ItemSalesRepository itemSalesRepository;

    @Autowired
    private NamedParameterJdbcTemplate template;

    @BeforeEach
    void setUp() {
        // テストデータ挿入
        String insertSql = """
            INSERT INTO users (id, name, email, password) VALUES
            (1, 'テストユーザー', 'test@example.com', 'password');

            INSERT INTO categories (id, name) VALUES
            (1, '文房具'),
            (2, '書籍');

            INSERT INTO items (id, name, price, category_id) VALUES
            (1, 'ペン', 100, 1),
            (2, 'ノート', 200, 1),
            (3, '小説A', 800, 2);

            INSERT INTO orders (id, user_id, order_date, total_price) VALUES
            (1, 1, '2024-07-01 10:00:00', 1100),
            (2, 1, '2024-07-15 15:30:00', 400),
            (3, 1, '2024-08-01 11:00:00', 800);

            INSERT INTO order_items (id, order_id, item_id, quantity) VALUES
            (1, 1, 1, 3), -- ペン 3個
            (2, 1, 3, 1), -- 小説A 1冊
            (3, 2, 2, 2), -- ノート 2冊
            (4, 3, 3, 1); -- 小説A 1冊 (期間外)
        """;
        template.getJdbcOperations().execute(insertSql);
    }

    @Test
    @DisplayName("指定期間内の商品別売上が正しく集計され、売上順にソートされていること")
    void findItemSales_success() {
        LocalDate from = LocalDate.of(2024, 7, 1);
        LocalDate to = LocalDate.of(2024, 7, 31);

        List<ItemSalesDTO> result = itemSalesRepository.findItemSales(from, to);

        assertEquals(3, result.size(), "3種類の商品が返されるはずです");

        // 1位: 小説A
        assertEquals("小説A", result.get(0).getItemName());
        assertEquals(1, result.get(0).getTotalQuantity());
        assertEquals(800, result.get(0).getTotalSales());

        // 2位: ノート
        assertEquals("ノート", result.get(1).getItemName());
        assertEquals(2, result.get(1).getTotalQuantity());
        assertEquals(400, result.get(1).getTotalSales());
        
        // 3位: ペン
        assertEquals("ペン", result.get(2).getItemName());
        assertEquals(3, result.get(2).getTotalQuantity());
        assertEquals(300, result.get(2).getTotalSales());
    }

    @Test
    @DisplayName("指定期間外のデータは集計に含まれないこと")
    void findItemSales_outOfRange() {
        LocalDate from = LocalDate.of(2024, 6, 1);
        LocalDate to = LocalDate.of(2024, 6, 30);

        List<ItemSalesDTO> result = itemSalesRepository.findItemSales(from, to);

        assertTrue(result.isEmpty(), "期間外なので結果は空のはずです");
    }

    @Test
    @DisplayName("データが存在しない期間を指定した場合、空のリストが返されること")
    void findItemSales_noData() {
        LocalDate from = LocalDate.of(2025, 1, 1);
        LocalDate to = LocalDate.of(2025, 1, 31);

        List<ItemSalesDTO> result = itemSalesRepository.findItemSales(from, to);

        assertTrue(result.isEmpty(), "データがないので結果は空のはずです");
    }
}
