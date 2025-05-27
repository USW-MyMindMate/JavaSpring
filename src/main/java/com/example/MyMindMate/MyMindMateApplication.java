package com.example.MyMindMate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MyMindMateApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyMindMateApplication.class, args);
	}

}
