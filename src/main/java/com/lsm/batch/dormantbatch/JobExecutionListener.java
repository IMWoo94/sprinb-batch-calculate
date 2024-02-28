package com.lsm.batch.dormantbatch;

public interface JobExecutionListener {

	void beforeJob(JobExecution jobExecution);

	void afterJob(JobExecution jobExecution);
}
