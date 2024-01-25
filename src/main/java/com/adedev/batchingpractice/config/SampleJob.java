package com.adedev.batchingpractice.config;

import com.adedev.batchingpractice.service.FirstTasklet;
import com.adedev.batchingpractice.service.SecondTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class SampleJob {

    private final JobRepository jobRepository;

    private final PlatformTransactionManager transactionManager;
    private final FirstTasklet firstTasklet;
    private final SecondTasklet secondTasklet;

    public SampleJob(JobRepository jobRepository, PlatformTransactionManager transactionManager, FirstTasklet firstTasklet, SecondTasklet secondTasklet) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.firstTasklet = firstTasklet;
        this.secondTasklet = secondTasklet;
    }


    @Bean
    public Job firstJob() {
        return new JobBuilder("First Job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(firstStep())
                .next(secondStep())
                .build();
    }

    private Step firstStep() {
        return new StepBuilder("first-step", jobRepository)
                .tasklet(firstTasklet, transactionManager)
                .build();
    }

    private Step secondStep() {
        return new StepBuilder("second-step", jobRepository)
                .tasklet(secondTasklet, transactionManager)
                .build();
    }

}
