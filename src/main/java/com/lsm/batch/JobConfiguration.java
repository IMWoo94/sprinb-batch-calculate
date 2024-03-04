package com.lsm.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
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
	@JobScope
	public Step step(
		JobRepository jobRepository,
		PlatformTransactionManager platformTransactionManager,
		Tasklet tasklet,
		@Value("#{jobParameters['name2']}") String name
	) {
		log.info("jobScope : {}", name);
		return new StepBuilder("step", jobRepository)
			.tasklet(tasklet, platformTransactionManager)
			.build();
	}

	@Bean
	@StepScope
	public Tasklet tasklet(@Value("#{jobParameters['name']}") String name) {
		return (a, b) -> {
			log.info("name : {}", name);
			return RepeatStatus.FINISHED;
		};
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
	// 			if (count == 20) {
	// 				return null;
	// 			}
	//
	// 			return count;
	// 		}
	// 	};
	//
	// 	ItemProcessor<Integer, Integer> itemProcessor = new ItemProcessor<>() {
	// 		@Override
	// 		public Integer process(Integer item) throws Exception {
	//
	// 			if (item == 15) {
	// 				throw new IllegalStateException(item.toString());
	// 			}
	// 			return item;
	// 		}
	// 	};
	//
	// 	return new StepBuilder("step", jobRepository)
	// 		.<Integer, Integer>chunk(10, platformTransactionManager)
	// 		.reader(itemReader)
	// 		.processor(itemProcessor)
	// 		.writer(read -> {
	// 		})
	// 		.faultTolerant()
	// 		.retry(IllegalStateException.class)
	// 		.retryLimit(3)
	// 		.build();
	// }
}
