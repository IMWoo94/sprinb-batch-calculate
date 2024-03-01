package com.lsm.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class JobConfiguration {

	@Bean
	public Job job(JobRepository jobRepository, Step step) {
		return new JobBuilder("job-chunk", jobRepository)
			.start(step)
			.build();
	}

	@Bean
	public Step step(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
		return new StepBuilder("step", jobRepository)
			.tasklet((contribution, chunkContext) -> {
				log.info("step 실행 {} , {}", contribution, chunkContext);
				return RepeatStatus.FINISHED;
			}, platformTransactionManager)
			.allowStartIfComplete(true) // 재시작 가능
			.startLimit(5)
			.build();
	}

	// @Bean
	// public Step step(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
	// 	ItemReader<Integer> itemReader = new ItemReader<>() {
	//
	// 		private int count = 0;
	//
	// 		@Override
	// 		public Integer read() throws
	// 			Exception,
	// 			UnexpectedInputException,
	// 			ParseException,
	// 			NonTransientResourceException {
	// 			count++;
	//
	// 			log.info("Read {}", count);
	//
	// 			if (count == 15) {
	// 				return null;
	// 			}
	//
	// 			return count;
	// 		}
	// 	};
	//
	// 	return new StepBuilder("step", jobRepository)
	// 		.chunk(10, platformTransactionManager)
	// 		.reader(itemReader)
	// 		// .processor()
	// 		.writer(read -> {
	// 		})
	// 		.build();
	// }
}
