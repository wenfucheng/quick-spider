package com.fangxuele.spiderproject;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.fangxuele.spiderproject.mapper")
public class SpiderProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpiderProjectApplication.class, args);
	}
}
