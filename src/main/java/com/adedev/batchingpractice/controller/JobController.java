package com.adedev.batchingpractice.controller;

import com.adedev.batchingpractice.request.JobParamsRequest;
import com.adedev.batchingpractice.service.JobService;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/job")
public class JobController {
    private final JobService jobService;
    private final JobOperator jobOperator;

    public JobController(JobService jobService, JobOperator jobOperator) {
        this.jobService = jobService;
        this.jobOperator = jobOperator;
    }

    @GetMapping("/start/{jobName}")
    public String startJob(@PathVariable String jobName, @RequestBody List<JobParamsRequest> jobParams) {
        jobService.startJob(jobName, jobParams);

        return "Job Started...";
    }

    @GetMapping("/stop/{executionId}")
    public String stopJob(@PathVariable long executionId) throws NoSuchJobExecutionException,
            JobExecutionNotRunningException {
        jobOperator.stop(executionId);
        return "Job Stopped...";
    }
}
