package com.example.sales_board.dto;

/**
 * 商品別売上集計をする(1件分)
 */
public class ItemSalesDTO {
    // 商品名
    private String itemName;
    // 商品別のトータル個数
    private Integer totalQuantity;
    // 商品別のトータル金額
    private Integer totalSales;
    
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public Integer getTotalQuantity() {
        return totalQuantity;
    }
    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
    public Integer getTotalSales() {
        return totalSales;
    }
    public void setTotalSales(Integer totalSales) {
        this.totalSales = totalSales;
    }
    @Override
    public String toString() {
        return "ItemSalesDTO [itemName=" + itemName + ", totalQuantity=" + totalQuantity + ", totalSales=" + totalSales
                + "]";
    }
}
