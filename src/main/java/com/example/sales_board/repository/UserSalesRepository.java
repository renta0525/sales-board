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

import com.example.sales_board.dto.UserSalesDTO;

@Repository
public class UserSalesRepository {
    
    @Autowired
    private NamedParameterJdbcTemplate template;

    public  List <UserSalesDTO> findUserSales(LocalDate from, LocalDate to) {
        String sql = """
            SELECT
                u.name AS user_name,
                SUM(o.total_price) AS total_sales
            FROM
                orders o
            JOIN users u ON o.user_id = u.id
            WHERE
                o.order_date BETWEEN :from AND :to
            GROUP BY
                u.name
            ORDER BY
                total_sales DESC
        """;
        SqlParameterSource param = new MapSqlParameterSource().addValue("from", from.atStartOfDay()).addValue("to", to.plusDays(1).atStartOfDay());
        return template.query(sql, param, new UserSalesRowMapper());
    }
    public class UserSalesRowMapper implements RowMapper<UserSalesDTO> {
        @Override
        public UserSalesDTO mapRow (ResultSet rs, int rowNum) throws SQLException {
            UserSalesDTO dto = new UserSalesDTO();
            dto.setUserName(rs.getString("user_name"));
            dto.setTotalSales(rs.getInt("total_sales"));
            return dto;
        }
    }
}
