package com.lsm.batch.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lsm.batch.dormantbatch.Job;

@Configuration
public class DormantBatchConfiguration {

	@Bean
	public Job dormantBatchJob(
		DormantBatchItemReader dormantBatchItemReader,
		DormantBatchItemProcessor dormantBatchItemProcessor,
		DormantBatchItemWriter dormantBatchItemWriter,
		DormantBatchJobExecutionListener dormantBatchJobExecutionListener
	) {

		return Job.builder()
			.itemReader(dormantBatchItemReader)
			.itemProcessor(dormantBatchItemProcessor)
			.itemWriter(dormantBatchItemWriter)
			.jobExecutionListener(dormantBatchJobExecutionListener)
			.build();
	}
}
