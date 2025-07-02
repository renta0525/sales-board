package com.example.sales_board.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.sales_board.dto.ItemSalesDTO;

@Repository
public class ItemSalesRepository {

    @Autowired
    private NamedParameterJdbcTemplate template;

    public List<ItemSalesDTO> findItemSales (LocalDate from, LocalDate to) {
        String sql = """
            SELECT
                i.name AS item_name,
                SUM(oi.quantity) AS total_quantity,
                SUM(oi.quantity * i.price) AS total_sales
            FROM
                orders o
            JOIN order_items oi ON o.id = oi.order_id
            JOIN items i ON oi.item_id = i.id
            WHERE
                o.order_date BETWEEN :from AND :to
            GROUP BY
                i.name
            ORDER BY
                total_sales DESC
        """;

        SqlParameterSource param = new MapSqlParameterSource().addValue("from", from.atStartOfDay()).addValue("to", to.plusDays(1).atStartOfDay());
        return template.query(sql, param, new ItemSalesRowMapper());
    }
    

    private static class ItemSalesRowMapper implements RowMapper<ItemSalesDTO>{
        @Override
        public ItemSalesDTO mapRow (ResultSet rs, int rowNum) throws SQLException {
            ItemSalesDTO dto = new ItemSalesDTO();
            dto.setItemName(rs.getString("item_name"));
            dto.setTotalQuantity(rs.getInt("total_quantity"));
            dto.setTotalSales(rs.getInt("total_sales"));
            return dto;
        }
    }
}
