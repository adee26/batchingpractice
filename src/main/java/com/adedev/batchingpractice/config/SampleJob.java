package com.adedev.batchingpractice.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class SampleJob {

    private final JobRepository jobRepository;

    private final PlatformTransactionManager transactionManager;

    public SampleJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }


    @Bean
    public Job firstJob() {
        return new JobBuilder("First Job", jobRepository)
                .start(firstStep())
                .next(secondStep())
                .build();
    }

    private Step firstStep() {
        return new StepBuilder("first-step", jobRepository)
                .tasklet(firstTask(), transactionManager)
                .build();
    }

    private Step secondStep() {
        return new StepBuilder("second-step", jobRepository)
                .tasklet(secondTask(), transactionManager)
                .build();
    }

    private Tasklet firstTask() {
        return (contribution, chunkContext) -> {
            System.out.println("This is the first tasklet step.");
            return RepeatStatus.FINISHED;
        };
    }

    private Tasklet secondTask() {
        return (contribution, chunkContext) -> {
            System.out.println("This is the second tasklet step.");
            return RepeatStatus.FINISHED;
        };
    }
}
