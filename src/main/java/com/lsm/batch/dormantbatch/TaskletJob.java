package com.lsm.batch.dormantbatch;

import java.time.LocalDateTime;
import java.util.Objects;

import lombok.Builder;

public class TaskletJob implements Job {

	private final Tasklet tasklet;
	private final JobExecutionListener jobExecutionListener;

	public TaskletJob(Tasklet tasklet) {
		this(tasklet, null);
	}

	@Builder
	public TaskletJob(
		ItemReader<?> itemReader,
		ItemProcessor<?, ?> itemProcessor,
		ItemWriter<?> itemWriter,
		JobExecutionListener jobExecutionListener
	) {
		this(new SimpleTasklet(itemReader, itemProcessor, itemWriter), jobExecutionListener);
	}

	public TaskletJob(Tasklet tasklet, JobExecutionListener jobExecutionListener) {
		this.tasklet = tasklet;
		this.jobExecutionListener = Objects.requireNonNullElseGet(jobExecutionListener,
			() -> new JobExecutionListener() {
				@Override
				public void beforeJob(JobExecution jobExecution) {

				}

				@Override
				public void afterJob(JobExecution jobExecution) {

				}
			});

	}

	@Override
	public JobExecution execute() {

		JobExecution jobExecution = new JobExecution();
		jobExecution.setStatus(BatchStatus.STARTING);
		jobExecution.setStartTime(LocalDateTime.now());

		jobExecutionListener.beforeJob(jobExecution);

		try {
			tasklet.execute();
			jobExecution.setStatus(BatchStatus.COMPLETED);
		} catch (Exception e) {
			jobExecution.setStatus(BatchStatus.FAILED);
		}

		jobExecution.setEndTime(LocalDateTime.now());

		jobExecutionListener.afterJob(jobExecution);
		return jobExecution;

	}
}
