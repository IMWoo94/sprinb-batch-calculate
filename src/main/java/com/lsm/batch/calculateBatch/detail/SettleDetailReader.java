package com.lsm.batch.calculateBatch.detail;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SettleDetailReader implements ItemReader<KeyAndCount>, StepExecutionListener {

	private Iterator<Map.Entry<Key, Long>> iterator;

	@Override
	public KeyAndCount read() throws
		Exception,
		UnexpectedInputException,
		ParseException,
		NonTransientResourceException {

		if (!iterator.hasNext()) {
			return null;
		}
		Map.Entry<Key, Long> map = iterator.next();
		return new KeyAndCount(map.getKey(), map.getValue());
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		JobExecution jobExecution = stepExecution.getJobExecution();
		ConcurrentHashMap<Key, Long> snapshots = (ConcurrentHashMap<Key, Long>)jobExecution.getExecutionContext()
			.get("snapshots");
		iterator = snapshots.entrySet().iterator();
	}
}
