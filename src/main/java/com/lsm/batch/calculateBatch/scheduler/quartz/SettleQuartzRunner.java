package com.lsm.batch.calculateBatch.scheduler.quartz;

import java.util.HashMap;
import java.util.Map;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.listeners.JobChainingJobListener;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SettleQuartzRunner extends QuartzJobRunner {

	private final Scheduler scheduler;

	@Override
	protected void doRun(ApplicationArguments args) {
		Map<String, String> params = new HashMap<>();

		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.putAll(params);

		JobDetail apiOrderGeneratorJob = JobBuilder.newJob(ApiOrderGeneratorQuartzJob.class)
			.withIdentity("apiOrderGeneratorJob", "settleBatch")
			.usingJobData(jobDataMap)
			.build();

		JobDetail settleJob = JobBuilder.newJob(SettleQuartzJob.class)
			.storeDurably(true)
			.withIdentity("settleQuartzJob", "settleBatch")
			.usingJobData(jobDataMap)
			.build();

		JobChainingJobListener chainListener = new JobChainingJobListener("ChainListener");
		chainListener.addJobChainLink(apiOrderGeneratorJob.getKey(), settleJob.getKey());

		Trigger trigger = buildJobCronTrigger("30 * * * * ?");

		try {
			scheduler.scheduleJob(apiOrderGeneratorJob, trigger);
			scheduler.addJob(settleJob, true);
			scheduler.getListenerManager().addJobListener(chainListener);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
}
