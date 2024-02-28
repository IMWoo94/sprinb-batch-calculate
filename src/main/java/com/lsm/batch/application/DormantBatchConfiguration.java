package com.lsm.batch.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lsm.batch.customer.Customer;
import com.lsm.batch.dormantbatch.Job;
import com.lsm.batch.dormantbatch.SimpleTasklet;

@Configuration
public class DormantBatchConfiguration {

	@Bean
	public Job dormantBatchJob(
		DormantBatchItemReader dormantBatchItemReader,
		DormantBatchItemProcessor dormantBatchItemProcessor,
		DormantBatchItemWriter dormantBatchItemWriter,
		DormantBatchJobExecutionListener dormantBatchJobExecutionListener
	) {
		SimpleTasklet<Customer, Customer> tasklet = new SimpleTasklet<>(dormantBatchItemReader,
			dormantBatchItemProcessor, dormantBatchItemWriter);
		
		return new Job(
			tasklet,
			dormantBatchJobExecutionListener
		);
	}
}
