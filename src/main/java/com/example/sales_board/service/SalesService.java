package com.example.sales_board.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.sales_board.dto.DailySalesDTO;
import com.example.sales_board.dto.ItemSalesDTO;
import com.example.sales_board.dto.UserSalesDTO;
import com.example.sales_board.repository.DailyRepository;
import com.example.sales_board.repository.ItemSalesRepository;
import com.example.sales_board.repository.UserSalesRepository;

@Service
public class SalesService {
    
    @Autowired
    private ItemSalesRepository itemSalesRepository;
    @Autowired
    private DailyRepository dailySalesRepository;
    @Autowired
    private UserSalesRepository userSalesRepository;

    public List<ItemSalesDTO> getItemSales(LocalDate from, LocalDate to) {
        return itemSalesRepository.findItemSales(from, to);
    }

    public List <DailySalesDTO> getDailySales(LocalDate from, LocalDate to) {
        return dailySalesRepository.findDailySales(from, to);
    }

    public List<UserSalesDTO> getUserSales(LocalDate from, LocalDate to) {
        return userSalesRepository.findUserSales(from, to);
    }
}
