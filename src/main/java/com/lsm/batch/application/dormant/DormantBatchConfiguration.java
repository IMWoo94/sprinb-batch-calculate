package com.lsm.batch.application.dormant;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lsm.batch.dormantbatch.Job;
import com.lsm.batch.dormantbatch.Step;
import com.lsm.batch.dormantbatch.StepJobBuilder;

@Configuration
public class DormantBatchConfiguration {

	@Bean
	public Job dormantBatchJob(
		Step preDormantBatchStep,
		Step dormantBatchStep,
		DormantBatchJobExecutionListener dormantBatchJobExecutionListener
	) {

		return new StepJobBuilder()
			.start(preDormantBatchStep)
			.next(dormantBatchStep)
			.build();
	}

	@Bean
	public Step dormantBatchStep(
		DormantBatchItemReader itemReader,
		DormantBatchItemProcessor itemProcessor,
		DormantBatchItemWriter itemWriter
	) {
		return Step.builder()
			.itemReader(itemReader)
			.itemProcessor(itemProcessor)
			.itemWriter(itemWriter)
			.build();
	}

	@Bean
	public Step preDormantBatchStep(
		PreDormantBatchItemReader itemReader,
		PreDormantBatchItemProcessor itemProcessor,
		PreDormantBatchItemWriter itemWriter
	) {
		return Step.builder()
			.itemReader(itemReader)
			.itemProcessor(itemProcessor)
			.itemWriter(itemWriter)
			.build();
	}
}
