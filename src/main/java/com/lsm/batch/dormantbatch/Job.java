package com.lsm.batch.dormantbatch;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class Job {

	private final Tasklet tasklet;
	private final JobExecutionListener jobExecutionListener;

	public Job(Tasklet tasklet, JobExecutionListener jobExecutionListener) {
		this.tasklet = tasklet;
		this.jobExecutionListener = jobExecutionListener;
	}

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
