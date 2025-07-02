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

import com.example.sales_board.dto.DailySalesDTO;

@Repository
public class DailyRepository {
    
    @Autowired
    private NamedParameterJdbcTemplate template;

    public List<DailySalesDTO> findDailySales (LocalDate from, LocalDate to) {
        String sql = """
            SELECT
                sub.order_date,
                SUM(sub.total_price) AS total_sales
            FROM (
                SELECT CAST(order_date AS DATE) AS order_date, total_price
                FROM orders
                WHERE order_date BETWEEN :from AND :to
            ) sub
            GROUP BY sub.order_date
            ORDER BY sub.order_date
        """;
        SqlParameterSource param = new MapSqlParameterSource().addValue("from", from.atStartOfDay()).addValue("to", to.plusDays(1).atStartOfDay());
        return template.query(sql, param, new DailySalesRowMapper());
    }

    private static class DailySalesRowMapper implements RowMapper<DailySalesDTO> {
        @Override
        public DailySalesDTO mapRow (ResultSet rs, int rowNum) throws SQLException {
            DailySalesDTO dto = new DailySalesDTO();
            dto.setDate(rs.getDate("order_date").toLocalDate());
            dto.setTotalSales(rs.getInt("total_sales"));
            return dto;
        }
    }
}
