package com.adedev.batchingpractice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BatchingPracticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchingPracticeApplication.class, args);
	}

}
