package com.xynu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xiaosuda
 */
@Controller
@SpringBootApplication(scanBasePackages = {"com.xynu"})
@MapperScan("com.xynu.mapper")
public class ChessApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChessApplication.class, args);
	}

	@RequestMapping("/")
	public String home() {
		return "redirect:/index/login";
	}
}
