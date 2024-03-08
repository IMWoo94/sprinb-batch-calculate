package com.lsm.batch.calculateBatch.runner;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class JobRunnerConfiguration {

	private final JobLauncher jobLauncher;
	private final Job settleJob;
	private int count;

	// 20초 후 반복
	@Scheduled(fixedDelay = 20000L)
	public void settleJobRun() throws
		JobInstanceAlreadyCompleteException,
		JobExecutionAlreadyRunningException,
		JobParametersInvalidException,
		JobRestartException {
		log.info("@Scheduled 20초 마다 반복 {}", count++);
		// JobParameters parameters = new JobParametersBuilder()
		// 	.addString("targetDate", "20240225")
		// 	.addString("totalCount", "500000")
		// 	.toJobParameters();
		//
		// jobLauncher.run(settleJob, parameters);

	}

}
