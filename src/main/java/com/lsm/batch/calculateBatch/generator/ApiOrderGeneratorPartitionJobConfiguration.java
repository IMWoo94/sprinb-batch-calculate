package com.lsm.batch.calculateBatch.generator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.lsm.batch.calculateBatch.domain.ApiOrder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApiOrderGeneratorPartitionJobConfiguration {

	/**
	 * STEP [ Master Step ]
	 * WorkerStep1, WorkerStep2, WorkerStep3, WorkerStep4, WorkerStep5, WorkerStep6, WorkerStep7
	 */
	private final JobRepository jobRepository;
	private final PlatformTransactionManager platformTransactionManager;

	@Bean
	public Job apiOrderGeneratorPartitionJob(Step masterStep) {
		return new JobBuilder("apiOrderGeneratorPartitionJob", jobRepository)
			.start(masterStep)
			.incrementer(new RunIdIncrementer())
			.validator(new DefaultJobParametersValidator(
				new String[] {"totalCount", "targetDate"}, new String[0])
			)
			.build();
	}

	@Bean
	@JobScope
	public Step masterStep(
		PartitionHandler apiOrderGeneratorPartitionHandler,
		@Value("#{jobParameters['targetDate']}") String targetDate,
		Step apiOrderGeneratorWorkerStep
	) {
		return new StepBuilder("masterStep", jobRepository)
			.partitioner("delegateStep", apiOrderGeneratorPartitioner(targetDate))
			.step(apiOrderGeneratorWorkerStep)
			.partitionHandler(apiOrderGeneratorPartitionHandler)
			.build();
	}

	// Master Step -> Worker Step 을 어떻게 다룰지 정의
	@Bean
	public PartitionHandler apiOrderGeneratorPartitionHandler(Step apiOrderGeneratorWorkerStep) {
		TaskExecutorPartitionHandler taskExecutorPartitionHandler = new TaskExecutorPartitionHandler();
		taskExecutorPartitionHandler.setStep(apiOrderGeneratorWorkerStep);
		taskExecutorPartitionHandler.setTaskExecutor(new SimpleAsyncTaskExecutor());
		taskExecutorPartitionHandler.setGridSize(7);
		return taskExecutorPartitionHandler;
	}

	// Worker Step 을 위해서 StepExecution 을 생성하는 인터페이스
	public Partitioner apiOrderGeneratorPartitioner(String targetDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate date = LocalDate.parse(targetDate, formatter);

		return new Partitioner() {
			@Override
			public Map<String, ExecutionContext> partition(int gridSize) {
				Map<String, ExecutionContext> result = new HashMap<>();

				// 0 ~ 6 targetDate 처리
				IntStream.range(0, 7)
					.forEach(it -> {
						ExecutionContext executionContext = new ExecutionContext();
						executionContext.putString("targetDate", date.minusDays(it).format(formatter));
						result.put("partition" + it, executionContext);
					});

				return result;
			}
		};

	}

	@Bean
	public Step apiOrderGeneratorWorkerStep(
		ItemReader<Boolean> apiOrderGeneratorReader,
		ApiOrderGeneratorProcessor apiOrderGeneratorProcessor,
		FlatFileItemWriter<ApiOrder> apiOrderGeneratorPartitionFlatFileItemWriter
	) {
		return new StepBuilder("apiOrderGeneratorWorkerStep", jobRepository)
			.<Boolean, ApiOrder>chunk(5000, platformTransactionManager)
			.reader(apiOrderGeneratorReader)
			.processor(apiOrderGeneratorProcessor)
			.writer(apiOrderGeneratorPartitionFlatFileItemWriter)
			.build();
	}

	@Bean
	@StepScope
	public FlatFileItemWriter<ApiOrder> apiOrderGeneratorPartitionFlatFileItemWriter(
		@Value("#{stepExecutionContext['targetDate']}") String targetDate
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
