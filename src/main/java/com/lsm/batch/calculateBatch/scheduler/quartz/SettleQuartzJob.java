package com.lsm.batch.calculateBatch.scheduler.quartz;

import org.quartz.JobDataMap;
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

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SettleQuartzJob extends QuartzJobBean {

	private final JobLauncher jobLauncher;
	private final Job settleJob;

	public SettleQuartzJob(JobLauncher jobLauncher, Job settleJob) {
		this.jobLauncher = jobLauncher;
		this.settleJob = settleJob;
	}

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getMergedJobDataMap();
		String targetDate = jobDataMap.get("targetDate").toString();
		JobParameters parameters = new JobParametersBuilder()
			.addString("targetDate", targetDate)
			.addString("totalCount", "1000")
			.toJobParameters();

		try {
			jobLauncher.run(settleJob, parameters);
		} catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException | JobRestartException |
				 JobParametersInvalidException e) {
			throw new RuntimeException(e);
		}
	}
}
