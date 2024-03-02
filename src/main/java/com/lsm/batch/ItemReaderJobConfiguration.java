package com.lsm.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ItemReaderJobConfiguration {

	@Bean
	public Job itemReaderJob(
		JobRepository jobRepository,
		Step itemReaderstep
	) {
		return new JobBuilder("itemReaderJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(itemReaderstep)
			.build();
	}

	@Bean
	public Step itemReaderstep(
		JobRepository jobRepository,
		PlatformTransactionManager platformTransactionManager,
		ItemReader<User> flatFileItemReader
	) {
		return new StepBuilder("step", jobRepository)
			.<User, User>chunk(2, platformTransactionManager)
			.reader(flatFileItemReader)
			.writer(System.out::println)
			.build();
	}

	@Bean
	public FlatFileItemReader<User> flatFileItemReader() {
		return new FlatFileItemReaderBuilder<User>()
			.name("flatFileItemReader")
			.resource(new ClassPathResource("users.txt"))
			.linesToSkip(2)
			.delimited().delimiter(",")
			.names("name", "age", "region", "telephone")
			.targetType(User.class)
			.strict(true)
			.build();
	}
}
