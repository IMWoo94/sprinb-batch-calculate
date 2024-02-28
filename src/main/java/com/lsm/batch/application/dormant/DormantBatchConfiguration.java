package com.lsm.batch.application.dormant;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lsm.batch.dormantbatch.Job;
import com.lsm.batch.dormantbatch.TaskletJob;

@Configuration
public class DormantBatchConfiguration {

	@Bean
	public Job dormantBatchJob(
		DormantBatchItemReader dormantBatchItemReader,
		DormantBatchItemProcessor dormantBatchItemProcessor,
		DormantBatchItemWriter dormantBatchItemWriter,
		DormantBatchJobExecutionListener dormantBatchJobExecutionListener
	) {

		return TaskletJob.builder()
			.itemReader(dormantBatchItemReader)
			.itemProcessor(dormantBatchItemProcessor)
			.itemWriter(dormantBatchItemWriter)
			.jobExecutionListener(dormantBatchJobExecutionListener)
			.build();
	}
}
