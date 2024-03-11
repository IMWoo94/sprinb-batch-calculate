package com.lsm.batch.calculateBatch.scheduler.quartz;

import java.util.HashMap;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.listeners.JobListenerSupport;

public class CustomJobChainingJobListener extends JobListenerSupport {

	private String name;
	private Map<JobKey, JobKey> chainLinks;

	public CustomJobChainingJobListener(String name) {
		this.name = name;
		chainLinks = new HashMap<JobKey, JobKey>();
	}

	@Override
	public String getName() {
		return name;
	}

	public void addJobChainLink(JobKey firstJob, JobKey secondJob) {

		if (firstJob == null || secondJob == null) {
			throw new IllegalArgumentException("Key cannot be null!");
		}

		if (firstJob.getName() == null || secondJob.getName() == null) {
			throw new IllegalArgumentException("Key cannot have a null name!");
		}

		chainLinks.put(firstJob, secondJob);
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		JobKey sj = chainLinks.get(context.getJobDetail().getKey());

		if (sj == null) {
			return;
		}

		getLog().info("Job '" + context.getJobDetail().getKey() + "' will now chain to Job '" + sj + "'");

		try {
			context.getScheduler().triggerJob(sj, context.getMergedJobDataMap());
		} catch (SchedulerException se) {
			getLog().error("Error encountered during chaining to Job '" + sj + "'", se);
		}
	}
}
