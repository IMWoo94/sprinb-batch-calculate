package com.lsm.batch.calculateBatch.scheduler.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class ApiOrderGeneratorQuartzJob extends QuartzJobBean {

	private final JobLauncher jobLauncher;
	private final Job apiOrderGeneratorPartitionJob;

	public ApiOrderGeneratorQuartzJob(JobLauncher jobLauncher, Job apiOrderGeneratorPartitionJob) {
		this.jobLauncher = jobLauncher;
		this.apiOrderGeneratorPartitionJob = apiOrderGeneratorPartitionJob;
	}

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		JobParameters parameters = new JobParametersBuilder()
			.addString("targetDate", "20240225")
			.addString("totalCount", "500000")
			.toJobParameters();

		try {
			jobLauncher.run(apiOrderGeneratorPartitionJob, parameters);
		} catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException | JobRestartException |
				 JobParametersInvalidException e) {
			throw new RuntimeException(e);
		}
	}
}
