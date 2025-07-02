package com.example.sales_board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/toLogin")
public class LoginController {
    
    @GetMapping("")
    public String index () {
        return "login/login";
    }
    
    @GetMapping("/login")
    public String main () {
        return "redirect:/sales-board/board";
    }
}
