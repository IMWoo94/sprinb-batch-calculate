package com.lsm.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ParallelStepJobConfiguration {

	/**
	 * flow1 ( Step1, Step2 )
	 * flow2 ( Step3 )
	 * 							-> Step4
	 */
	@Bean
	public Job parallelStepJob(
		JobRepository jobRepository,
		Flow splitFlow,
		Step basicStep4
	) {
		return new JobBuilder("parallelStepJob", jobRepository)
			.start(splitFlow)
			.next(basicStep4)
			.build()
			.build();
	}

	@Bean
	public Flow splitFlow(Flow flow1, Flow flow2) {
		return new FlowBuilder<SimpleFlow>("splitFlow")
			.split(new SimpleAsyncTaskExecutor())
			.add(flow1, flow2)
			.build();
	}

	@Bean
	public Flow flow1(Step parallelStep1, Step parallelStep2) {
		return new FlowBuilder<SimpleFlow>("flow1")
			.start(parallelStep1)
			.next(parallelStep2)
			.build();
	}

	@Bean
	public Flow flow2(Step parallelStep3) {
		return new FlowBuilder<SimpleFlow>("flow2")
			.start(parallelStep3)
			.build();
	}

	@Bean
	public Step parallelStep1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
		return new StepBuilder("parallelStep1", jobRepository)
			.tasklet((a, b) -> {
				Thread.sleep(1000);
				log.info("parallelStep1");
				return RepeatStatus.FINISHED;
			}, platformTransactionManager)
			.build();
	}

	@Bean
	public Step parallelStep2(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
		return new StepBuilder("parallelStep2", jobRepository)
			.tasklet((a, b) -> {
				Thread.sleep(2000);
				log.info("parallelStep2");
				return RepeatStatus.FINISHED;
			}, platformTransactionManager)
			.build();
	}

	@Bean
	public Step parallelStep3(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
		return new StepBuilder("parallelStep3", jobRepository)
			.tasklet((a, b) -> {
				Thread.sleep(2500);
				log.info("parallelStep3");
				return RepeatStatus.FINISHED;
			}, platformTransactionManager)
			.build();
	}

	@Bean
	public Step basicStep4(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
		return new StepBuilder("basicStep4", jobRepository)
			.tasklet((a, b) -> {
				Thread.sleep(1000);
				log.info("basicStep4");
				return RepeatStatus.FINISHED;
			}, platformTransactionManager)
			.build();
	}

}
