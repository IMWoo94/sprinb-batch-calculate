package com.lsm.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ItemWriterJobConfiguration {

	@Bean
	public Job itemWriterJob(
		JobRepository jobRepository,
		Step itemWriterstep
	) {
		return new JobBuilder("itemWriterJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(itemWriterstep)
			.build();
	}

	@Bean
	public Step itemWriterstep(
		JobRepository jobRepository,
		PlatformTransactionManager platformTransactionManager,
		ItemReader<User> flatFileItemReader,
		ItemWriter<User> jpaItemWriter
	) {
		return new StepBuilder("step", jobRepository)
			.<User, User>chunk(2, platformTransactionManager)
			.reader(flatFileItemReader)
			.writer(jpaItemWriter)
			.build();
	}

	@Bean
	public ItemWriter<User> flatFileItemWriter() {
		log.info("flatFileItemWriter");
		return new FlatFileItemWriterBuilder<User>()
			.name("flatFileItemWriter")
			.resource(new PathResource("src/main/resources/new_users.txt"))
			.delimited().delimiter("__")
			.names("name", "age", "region", "telephone")
			.build();
	}

	@Bean
	public ItemWriter<User> formattedFlatFileItemWriter() {
		log.info("formattedFlatFileItemWriter");
		return new FlatFileItemWriterBuilder<User>()
			.name("formattedFlatFileItemWriter")
			.resource(new PathResource("src/main/resources/new_formatted_users.txt"))
			.formatted()
			.format("%s의 나이는 %d 입니다. 사는 곳은 %s, 전화번호는 %s 입니다.")
			.names("name", "age", "region", "telephone")
			.shouldDeleteIfExists(false)
			.append(true)
			.build();
	}

	@Bean
	public ItemWriter<User> JsonItemWriter() {
		return new JsonFileItemWriterBuilder<User>()
			.name("JsonItemWriter")
			.resource(new PathResource("src/main/resources/new_users.json"))
			.jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
			.build();
	}

	@Bean
	public ItemWriter<User> jpaItemWriter(
		EntityManagerFactory entityManagerFactory
	) {
		return new JpaItemWriterBuilder<User>()
			.entityManagerFactory(entityManagerFactory)
			.build();
	}

}
