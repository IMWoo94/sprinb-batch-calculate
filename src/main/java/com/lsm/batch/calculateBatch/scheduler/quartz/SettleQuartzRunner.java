package com.lsm.batch.calculateBatch.scheduler.quartz;

import java.util.HashMap;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SettleQuartzRunner extends QuartzJobRunner {

	private final Scheduler scheduler;

	@Override
	protected void doRun(ApplicationArguments args) {
		JobDetail jobDetail = buildJobDetail(SettleQuartzJob.class, "settleQuartzJob", "settleBatch", new HashMap());
		Trigger trigger = buildJobCronTrigger("30 * * * * ?");
		try {
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
}
