package com.lsm.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class MultiThreadedJobConfiguration {

	@Bean
	public Job multiThreadedJob(
		JobRepository jobRepository,
		Step multiThreadedStep
	) {
		return new JobBuilder("multiThreadedJob", jobRepository)
			.start(multiThreadedStep)
			.incrementer(new RunIdIncrementer())
			.build();
	}

	@Bean
	public Step multiThreadedStep(
		JobRepository jobRepository,
		PlatformTransactionManager platformTransactionManager,
		ItemReader<User> jpaPagingItemReader
	) {
		return new StepBuilder("multiThreadedStep", jobRepository)
			.<User, User>chunk(5, platformTransactionManager)
			.reader(jpaPagingItemReader) // Thread-safe 한 구현체 사용
			.writer(result -> log.info(result.toString()))
			.taskExecutor(new SimpleAsyncTaskExecutor())
			.build();
	}
}
