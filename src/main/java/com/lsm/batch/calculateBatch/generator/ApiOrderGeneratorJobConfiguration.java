package com.lsm.batch.calculateBatch.generator;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.lsm.batch.calculateBatch.domain.ApiOrder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApiOrderGeneratorJobConfiguration {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager platformTransactionManager;

	@Bean
	public Job apiOrderGeneratorJob(Step apiOrderGeneratorStep) {
		return new JobBuilder("apiOrderGeneratorJob", jobRepository)
			.start(apiOrderGeneratorStep)
			.incrementer(new RunIdIncrementer())
			.build();
	}

	@Bean
	public Step apiOrderGeneratorStep(
		ItemReader<Boolean> apiOrderGeneratorReader,
		ApiOrderGeneratorProcessor apiOrderGeneratorProcessor,
		FlatFileItemWriter<ApiOrder> apiOrderFlatFileItemWriter
	) {
		return new StepBuilder("apiOrderGeneratorStep", jobRepository)
			.<Boolean, ApiOrder>chunk(1000, platformTransactionManager)
			.reader(apiOrderGeneratorReader)
			.processor(apiOrderGeneratorProcessor)
			.writer(apiOrderFlatFileItemWriter)
			.build();
	}

	@Bean
	@StepScope
	public FlatFileItemWriter<ApiOrder> apiOrderFlatFileItemWriter(
		@Value("#{jobParameters['targetDate']}") String targetDate
	) {
		String fileName = targetDate + "_api_orders.csv";

		return new FlatFileItemWriterBuilder<ApiOrder>()
			.name("apiOrderFlatFileItemWriter")
			.resource(new PathResource("src/main/resources/datas/" + fileName))
			.delimited()
			.names("id", "customerId", "url", "state", "createdAt")
			.headerCallback(writer -> writer.write("id,customerId,url,state,createdAt"))
			.build();
	}
}
