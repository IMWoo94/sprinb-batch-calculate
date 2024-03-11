package com.lsm.batch.calculateBatch.scheduler.quartz;

import java.util.HashMap;
import java.util.Map;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SettleQuartzRunner extends QuartzJobRunner {

	private final Scheduler scheduler;

	@Override
	protected void doRun(ApplicationArguments args) {
		log.info("SettleQuartzRunner doRun");
		Map<String, String> params = new HashMap<>();
		params.put("targetDate", "20240305");
		params.put("totalCount", "500000");

		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.putAll(params);

		JobDetail apiOrderGeneratorJob = JobBuilder.newJob(ApiOrderGeneratorQuartzJob.class)
			.withIdentity("apiOrderGeneratorJob", "settleBatch")
			.usingJobData(jobDataMap)
			.build();

		JobDetail settleJob = JobBuilder.newJob(SettleQuartzJob.class)
			.storeDurably(true)
			.withIdentity("settleQuartzJob", "settleBatch")
			.setJobData(apiOrderGeneratorJob.getJobDataMap())
			.build();

		CustomJobChainingJobListener chainListener = new CustomJobChainingJobListener("ChainListener");
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
