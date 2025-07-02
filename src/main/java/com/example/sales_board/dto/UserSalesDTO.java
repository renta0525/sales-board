package com.example.sales_board.dto;

/**
 * ユーザー別売上集計
 */
public class UserSalesDTO {
    private String userName;
    private Integer totalSales;
    
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public Integer getTotalSales() {
        return totalSales;
    }
    public void setTotalSales(Integer totalSales) {
        this.totalSales = totalSales;
    }
    
    @Override
    public String toString() {
        return "UserSalesDTO [userName=" + userName + ", totalSales=" + totalSales + "]";
    }
}
