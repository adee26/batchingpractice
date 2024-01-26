package com.adedev.batchingpractice.config;

import com.adedev.batchingpractice.chunk.processor.FirstItemProcessor;
import com.adedev.batchingpractice.chunk.reader.FirstItemReader;
import com.adedev.batchingpractice.chunk.writer.FirstItemWriter;
import com.adedev.batchingpractice.listener.FirstJobListener;
import com.adedev.batchingpractice.listener.FirstStepListener;
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
    private final FirstJobListener firstJobListener;
    private final FirstStepListener firstStepListener;
    private final FirstItemReader firstItemReader;
    private final FirstItemProcessor firstItemProcessor;
    private final FirstItemWriter firstItemWriter;

    public SampleJob(JobRepository jobRepository, PlatformTransactionManager transactionManager, FirstTasklet firstTasklet, SecondTasklet secondTasklet, FirstJobListener firstJobListener, FirstStepListener firstStepListener, FirstItemReader firstItemReader, FirstItemProcessor firstItemProcessor, FirstItemWriter firstItemWriter) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.firstTasklet = firstTasklet;
        this.secondTasklet = secondTasklet;
        this.firstJobListener = firstJobListener;
        this.firstStepListener = firstStepListener;
        this.firstItemReader = firstItemReader;
        this.firstItemProcessor = firstItemProcessor;
        this.firstItemWriter = firstItemWriter;
    }


    //@Bean commented as not needed at the moment
    public Job firstJob() {
        return new JobBuilder("First Job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(firstStep())
                .listener(firstJobListener)
                .next(secondStep())
                .build();
    }

    @Bean
    public Job secondJob() {
        return new JobBuilder("Second Job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(firstChunkStep())
                .next(secondStep())
                .build();
    }

    private Step firstChunkStep() {
        return new StepBuilder("First Chunk Step", jobRepository)
                .<Integer, Long> chunk(3, transactionManager)
                .reader(firstItemReader)
                .processor(firstItemProcessor)
                .writer(firstItemWriter)
                .build();
    }

    private Step firstStep() {
        return new StepBuilder("first-step", jobRepository)
                .tasklet(firstTasklet, transactionManager)
                .listener(firstStepListener)
                .build();
    }

    private Step secondStep() {
        return new StepBuilder("second-step", jobRepository)
                .tasklet(secondTasklet, transactionManager)
                .build();
    }

}
