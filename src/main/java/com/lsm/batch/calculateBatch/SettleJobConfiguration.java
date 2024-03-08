package com.lsm.batch.calculateBatch;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lsm.batch.calculateBatch.support.DateFormatJobParametersValidator;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SettleJobConfiguration {

	private final JobRepository jobRepository;

	// 일일 정산 배치
	@Bean
	public Job settleJob(
		Step preSettleDetailStep,
		Step settleDetailStep,
		Step settleGroupStep
	) {
		return new JobBuilder("settleJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.validator(new DateFormatJobParametersValidator(new String[] {"targetDate"}))
			.start(preSettleDetailStep)
			.next(settleDetailStep)
			.next(isSundayDecider()).on("COMPLETED").to(settleGroupStep).build()
			.build();
	}

	/**
	 * 주간 정산 배치 [ 1주일간의 데이터를 집계하여 DB 에 쌓고 고객사에 이메일 전송 ]
	 * Job 의 흐름을 제어하는 방식으로 Flow 를 사용하는 것도 있지만, 더욱 간결하게 표현할 수 있는 것으로 JobExecutionDecider 가 있다.
	 * 매주 일요일마다 주간 정산을 한다.
	 */
	public JobExecutionDecider isSundayDecider() {
		return new JobExecutionDecider() {
			@Override
			public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
				String targetDate = stepExecution.getJobParameters().getString("targetDate");
				LocalDate date = LocalDate.parse(targetDate, formatter);

				if (date.getDayOfWeek() != DayOfWeek.SUNDAY) {
					return new FlowExecutionStatus("NOOP");
				}
				return FlowExecutionStatus.COMPLETED;
			}
		};

	}

}
