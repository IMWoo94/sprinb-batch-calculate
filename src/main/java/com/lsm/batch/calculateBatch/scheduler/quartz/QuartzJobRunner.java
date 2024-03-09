package com.lsm.batch.calculateBatch.scheduler.quartz;

import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

public abstract class QuartzJobRunner implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments args) throws Exception {
		doRun(args);
	}

	public Trigger buildJobCronTrigger(String scheduleExp) {
		return TriggerBuilder
			.newTrigger()
			.withSchedule(CronScheduleBuilder.cronSchedule(scheduleExp))
			.build();
	}

	public JobDetail buildJobDetail(Class job, String name, String group, Map params) {
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.putAll(params);

		return JobBuilder.newJob(job)
			.withIdentity(name, group)
			.usingJobData(jobDataMap)
			.build();
	}

	protected abstract void doRun(ApplicationArguments args);
}
