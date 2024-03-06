package com.lsm.batch.calculateBatch.detail;

import java.util.concurrent.ConcurrentMap;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class testReader implements ItemReader<Key>, StepExecutionListener {

	private StepExecution stepExecution;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
	}

	@Override
	public Key read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		JobExecution jobExecution = this.stepExecution.getJobExecution();
		ExecutionContext executionContext = jobExecution.getExecutionContext();
		
		ConcurrentMap<Key, Long> snapshotMap = (ConcurrentMap<Key, Long>)executionContext.get("snapshots");

		log.info(snapshotMap.toString());
		return null;
	}
}
