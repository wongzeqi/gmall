package com.wjq.gmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.wjq.gmall.cart.mapper")
public class GmallCartServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(GmallCartServiceApplication.class, args);
    }

}
