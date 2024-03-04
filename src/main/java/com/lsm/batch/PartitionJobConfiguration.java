package com.lsm.batch;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class PartitionJobConfiguration {

	@Bean
	public Job partitionJob(
		JobRepository jobRepository,
		Step managerStep
	) {
		return new JobBuilder("partitionJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(managerStep)
			.build();
	}

	@Bean
	public Step managerStep(
		JobRepository jobRepository,
		Step workerStep,
		PartitionHandler partitionHandler,
		Partitioner ColumnRangePartitioner
	) {
		return new StepBuilder("managerStep", jobRepository)
			.partitioner("delegateStep", ColumnRangePartitioner)
			.step(workerStep)
			.partitionHandler(partitionHandler)
			.build();
	}

	@Bean
	public PartitionHandler partitionHandler(Step workerStep) {
		// 로컬 환경에서 멀티스레드로 수행할 수 있도록 TaskExecutorPartitionHandler 구현체 사용
		TaskExecutorPartitionHandler taskExecutorPartitionHandler = new TaskExecutorPartitionHandler();
		// Worker 로 실행할 Step 지정
		taskExecutorPartitionHandler.setStep(workerStep);
		// Step 을 실행하기 위한 실행기 지정
		taskExecutorPartitionHandler.setTaskExecutor(new SimpleAsyncTaskExecutor());
		// Worker 의 갯수 지정
		taskExecutorPartitionHandler.setGridSize(5);
		return taskExecutorPartitionHandler;
	}

	@Bean
	public Step workerStep(
		JobRepository jobRepository,
		JpaPagingItemReader<User> jpaPagingItemPartitionReader,
		PlatformTransactionManager platformTransactionManager
	) {
		return new StepBuilder("workerStep", jobRepository)
			.<User, User>chunk(2, platformTransactionManager)
			.reader(jpaPagingItemPartitionReader)
			.writer(result -> log.info(result.toString()))
			.build();
	}

	@Bean
	@StepScope
	public JpaPagingItemReader<User> jpaPagingItemPartitionReader(
		@Value("#{stepExecutionContext[minValue]}") Long minValue,
		@Value("#{stepExecutionContext[maxValue]}") Long maxValue,
		@Value("#{stepExecutionContext[name]}") String partitionName,
		EntityManagerFactory entityManagerFactory
	) {
		log.info("name : {}, minValue : {}, maxValue : {}", partitionName, minValue, maxValue);

		Map<String, Object> params = new HashMap<>();
		params.put("minValue", minValue);
		params.put("maxValue", maxValue);
		return new JpaPagingItemReaderBuilder<User>()
			.name("jpaPagingItemPartitionReader")
			.entityManagerFactory(entityManagerFactory)
			.pageSize(5)
			.queryString("""
					select u from User u where u.id between :minValue and :maxValue
				""")
			.parameterValues(params)
			.build();
	}
}
