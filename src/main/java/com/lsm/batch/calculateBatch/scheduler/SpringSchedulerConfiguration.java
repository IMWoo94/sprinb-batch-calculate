package com.lsm.batch.calculateBatch.scheduler;

import java.time.LocalDateTime;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
@EnableScheduling
public class SpringSchedulerConfiguration implements SchedulingConfigurer {

	private final JobLauncher jobLauncher;
	private final Job settleJob;

	// 매주 23시
	private static final String CRON_EXPRESSION_EXAMPLE = "* * 23 * * *";

	// 20 초 마다 정산 배치 프로세스
	@Scheduled(cron = CRON_EXPRESSION_EXAMPLE)
	public void settleJobRun() throws
		JobInstanceAlreadyCompleteException,
		JobExecutionAlreadyRunningException,
		JobParametersInvalidException,
		JobRestartException {
		JobParameters parameters = new JobParametersBuilder()
			.addString("targetDate", "20240225")
			.addString("totalCount", "500000")
			.toJobParameters();

		jobLauncher.run(settleJob, parameters);
	}

	// @Scheduled(fixedRate = 3000)
	public void fixedRate1() throws InterruptedException {
		LocalDateTime start = LocalDateTime.now();
		log.info(">>>>>fixedRate1 시작 시간 {}", start);
		Thread.sleep(5000);
		LocalDateTime end = LocalDateTime.now();
		int total = end.getSecond() - start.getSecond();
		log.info(">>>>>fixedRate1 종료 시간 {} , 총 소요 시간 : {}", end, total);
	}

	// @Scheduled(fixedRate = 3000)
	public void fixedRate2() throws InterruptedException {
		LocalDateTime start = LocalDateTime.now();
		log.info(">>>>>fixedRate2 시작 시간 {}", start);
		Thread.sleep(5000);
		LocalDateTime end = LocalDateTime.now();
		int total = end.getSecond() - start.getSecond();
		log.info(">>>>>fixedRate2 종료 시간 {} , 총 소요 시간 : {}", end, total);
	}

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(5);
		threadPoolTaskScheduler.setThreadNamePrefix("thread-");
		threadPoolTaskScheduler.initialize();

		taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
	}
}
