package com.adedev.batchingpractice.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.adedev.batchingpractice.config.JobConstants.FIRST_JOB;
import static com.adedev.batchingpractice.config.JobConstants.SECOND_JOB;

@RestController
@RequestMapping("/api/job")
public class JobController {

    private final JobLauncher jobLauncher;
    @Qualifier("firstJob")
    private final Job firstJob;
    @Qualifier("secondJob")
    private final Job secondJob;

    public JobController(JobLauncher jobLauncher, Job firstJob, Job secondJob) {
        this.jobLauncher = jobLauncher;
        this.firstJob = firstJob;
        this.secondJob = secondJob;
    }

    @GetMapping("/start/{jobName}")
    public String startJob(@PathVariable String jobName) throws JobInstanceAlreadyCompleteException,
            JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        Map<String, JobParameter<?>> params = new HashMap<>();
        params.put("currentTime", new JobParameter(System.currentTimeMillis(), Long.class));

        JobParameters jobParameters = new JobParameters(params);

        if(FIRST_JOB.equals(jobName)) {
            jobLauncher.run(firstJob, jobParameters);
        }else if(SECOND_JOB.equals(jobName)) {
            jobLauncher.run(secondJob, jobParameters);
        }
        return "Job Started...";
    }
}
