package com.lsm.batch.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lsm.batch.dormantbatch.Job;

@Configuration
public class DormantBatchConfiguration {

	@Bean
	public Job dormantBatchJob(
		DormantBatchTasklet dormantBatchTasklet,
		DormantBatchJobExecutionListener dormantBatchJobExecutionListener
	) {
		return new Job(
			dormantBatchTasklet,
			dormantBatchJobExecutionListener
		);
	}
}
