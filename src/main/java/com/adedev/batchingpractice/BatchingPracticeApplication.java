package com.adedev.batchingpractice;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.adedev.batchingpractice.config")
public class BatchingPracticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchingPracticeApplication.class, args);
	}

}
