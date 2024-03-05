package com.lsm.batch.calculateBatch.generator;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * JobParameter 로 주어진 Count 만큼 데이터를 읽어오는 것처럼 만들기
 */
@Component
@StepScope
public class ApiOrderGeneratorReader implements ItemReader<Boolean> {

	private Long totalCount;
	private AtomicLong current;

	public ApiOrderGeneratorReader(
		@Value("#{jobParameters['totalCount']}") String totalCount
	) {
		this.totalCount = Long.parseLong(totalCount);
		this.current = new AtomicLong(0);
	}

	@Override
	public Boolean read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		if (current.incrementAndGet() > totalCount) {
			return null;
		}
		return true;
	}
}
