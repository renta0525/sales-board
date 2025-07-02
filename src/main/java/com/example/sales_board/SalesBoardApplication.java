package com.example.sales_board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SalesBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalesBoardApplication.class, args);

		// String rawPassword = "aaa";
		// String encoded = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(rawPassword);
		// System.out.println("ハッシュ化されたパスワード: " + encoded);
	}
}
