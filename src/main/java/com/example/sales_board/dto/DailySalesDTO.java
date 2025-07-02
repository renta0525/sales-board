package com.example.sales_board.dto;

import java.time.LocalDate;

/**
 * 日別売上集計
 */
public class DailySalesDTO {
    // 日付
    private LocalDate date;
    // 日付別のトータル金額
    private Integer totalSales;
    
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public Integer getTotalSales() {
        return totalSales;
    }
    public void setTotalSales(Integer totalSales) {
        this.totalSales = totalSales;
    }
    
    @Override
    public String toString() {
        return "DailySalesDto [date=" + date + ", totalSales=" + totalSales + "]";
    }
}
