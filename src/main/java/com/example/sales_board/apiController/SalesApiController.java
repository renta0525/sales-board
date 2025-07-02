package com.example.sales_board.apiController;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.sales_board.dto.DailySalesDTO;
import com.example.sales_board.dto.ItemSalesDTO;
import com.example.sales_board.dto.UserSalesDTO;
import com.example.sales_board.service.SalesService;

/**
 * 各api
 */
@RestController
@RequestMapping("/api/sales")
@CrossOrigin(origins = "http://localhost:5173")
public class SalesApiController {
    
    @Autowired
    private SalesService salesService;

    /**
     * 商品別売上集計をする(1件分)
     * @param from
     * @param to
     * @return
     */
    @GetMapping("/by-item")
    public List<ItemSalesDTO> getItemSales (
                                            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to)
                                            {
        return salesService.getItemSales(from, to);
    }

    /**
     * 日別売上集計
     * @param from
     * @param to
     * @return
     */
    @GetMapping("/by-daily")
    public List<DailySalesDTO> getDailySales (
                                            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to)
                                            {
        return salesService.getDailySales(from, to);
    }

    /**
     *   ユーザー別売上集計
     * @param from
     * @param to
     * @return
     */
    @GetMapping("/by-user")
    public List<UserSalesDTO> getUserSales (
                                            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to)
                                            {
        return salesService.getUserSales(from, to);
    }
}
