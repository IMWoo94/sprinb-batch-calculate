package com.lsm.batch.calculateBatch.scheduler.quartz;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
public class ApiOrderGeneratorQuartzJob extends QuartzJobBean {

	private final JobLauncher jobLauncher;
	private final Job apiOrderGeneratorPartitionJob;

	private static int count = 0;

	private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

	public ApiOrderGeneratorQuartzJob(JobLauncher jobLauncher, Job apiOrderGeneratorPartitionJob) {
		log.info("ApiOrderGeneratorQuartzJob Construct : {}", count);
		this.jobLauncher = jobLauncher;
		this.apiOrderGeneratorPartitionJob = apiOrderGeneratorPartitionJob;
	}

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getMergedJobDataMap();

		LocalDate date = LocalDate.parse(jobDataMap.get("targetDate").toString(), dateTimeFormatter);
		LocalDate localDate = date.plusDays(count++);
		String targetDate = localDate.format(dateTimeFormatter);
		jobDataMap.put("targetDate", targetDate);

		JobParameters parameters = new JobParametersBuilder()
			.addString("targetDate", targetDate)
			.addString("totalCount", "1000")
			.toJobParameters();

		try {
			jobLauncher.run(apiOrderGeneratorPartitionJob, parameters);
		} catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException | JobRestartException |
				 JobParametersInvalidException e) {
			throw new RuntimeException(e);
		}
	}
}
