package com.lsm.batch;

import java.util.Arrays;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ItemProcessorJobConfiguration {

	@Bean
	public Job itemProcessorJob(
		JobRepository jobRepository,
		Step itemProcessorstep
	) {
		return new JobBuilder("itemProcessorJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(itemProcessorstep)
			.build();
	}

	@Bean
	public Step itemProcessorstep(
		JobRepository jobRepository,
		PlatformTransactionManager platformTransactionManager,
		ItemReader<User> flatFileItemReader
	) {
		final List<ItemProcessor<User, User>> list = Arrays.asList(customProcessor1(), customProcessor2(),
			customProcessor3());

		return new StepBuilder("step", jobRepository)
			.<User, User>chunk(2, platformTransactionManager)
			.reader(flatFileItemReader)
			.processor(new CompositeItemProcessor<>(list))
			.writer(System.out::println)
			.build();
	}

	private ItemProcessor<User, String> customProcessor() {
		return item -> {
			if (item.getName().equals("민수"))
				return null;
			return "%s의 나이는 %s 입니다. 사는 곳은 %s, 전환번호는 %s 입니다.".formatted(item.getName(), item.getAge(),
				item.getRegion(), item.getTelephone());
		};
	}

	private ItemProcessor<User, User> customProcessor1() {
		return item -> {
			item.setName(item.getName() + item.getName());
			return item;
		};
	}

	private ItemProcessor<User, User> customProcessor2() {
		return item -> {
			item.setAge(item.getAge() + item.getAge());
			return item;
		};
	}

	private ItemProcessor<User, User> customProcessor3() {
		return item -> {
			item.setRegion(item.getRegion() + item.getRegion());
			return item;
		};
	}
}
