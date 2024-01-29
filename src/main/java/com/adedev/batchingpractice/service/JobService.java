package com.adedev.batchingpractice.service;

import com.adedev.batchingpractice.request.JobParamsRequest;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.adedev.batchingpractice.config.JobConstants.FIRST_JOB;
import static com.adedev.batchingpractice.config.JobConstants.SECOND_JOB;

@Service
public class JobService {
    private final JobLauncher jobLauncher;
    @Qualifier("firstJob")
    private final Job firstJob;
    @Qualifier("secondJob")
    private final Job secondJob;

    public JobService(JobLauncher jobLauncher, Job firstJob, Job secondJob) {
        this.jobLauncher = jobLauncher;
        this.firstJob = firstJob;
        this.secondJob = secondJob;
    }

    @Async
    public void startJob(String jobName, List<JobParamsRequest> jobParams) {
        Map<String, JobParameter<?>> params = new HashMap<>();
        params.put("currentTime", new JobParameter(System.currentTimeMillis(), Long.class));

        jobParams.forEach(jobParam -> params.put(jobParam.getParamKey(),
                new JobParameter<>(jobParam.getParamValue(), String.class)));

        JobParameters jobParameters = new JobParameters(params);
        JobExecution jobExecution;

        if(FIRST_JOB.equals(jobName)) {
            try {
                jobExecution = jobLauncher.run(firstJob, jobParameters);
                System.out.println("jobExecution ID = " + jobExecution.getId());
            } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                     JobParametersInvalidException e) {
                throw new RuntimeException(e);
            }
        }else if(SECOND_JOB.equals(jobName)) {
            try {
                jobExecution = jobLauncher.run(secondJob, jobParameters);
                System.out.println("jobExecution ID = " + jobExecution.getId());
            } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                     JobParametersInvalidException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
