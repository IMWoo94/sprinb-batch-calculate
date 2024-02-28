package com.lsm.batch.application.step;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lsm.batch.dormantbatch.Job;
import com.lsm.batch.dormantbatch.Step;
import com.lsm.batch.dormantbatch.StepJobBuilder;

@Configuration
public class StepExampleBatchConfiguration {

	@Bean
	public Job stepExampleBatchJob(
		Step step1,
		Step step2,
		Step step3
	) {
		return new StepJobBuilder()
			.start(step1)
			.next(step2)
			.next(step3)
			.build();
	}

	@Bean
	public Step step1() {
		return new Step(
			() -> System.out.println("step1")
		);
	}

	@Bean
	public Step step2() {
		return new Step(
			() -> System.out.println("step2")
		);
	}

	@Bean
	public Step step3() {
		return new Step(
			() -> System.out.println("step3")
		);
	}

}
