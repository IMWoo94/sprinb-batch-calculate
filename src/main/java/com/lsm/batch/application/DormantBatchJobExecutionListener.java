package com.lsm.batch.application;

import org.springframework.stereotype.Component;

import com.lsm.batch.EmailProvider;
import com.lsm.batch.dormantbatch.JobExecution;
import com.lsm.batch.dormantbatch.JobExecutionListener;

@Component
public class DormantBatchJobExecutionListener implements JobExecutionListener {

	private final EmailProvider emailProvider;

	public DormantBatchJobExecutionListener() {
		this.emailProvider = new EmailProvider.Fake();
	}

	@Override
	public void beforeJob(JobExecution jobExecution) {

	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		// 비즈니스 로직
		emailProvider.send(
			"admin@test.com",
			"배치 완료 알림",
			"DormantBatchJob 이 수행되었습니다. " + jobExecution.toString()
		);
	}
}
