package com.lsm.batch.calculateBatch.detail;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.lsm.batch.calculateBatch.domain.ServicedPolicy;
import com.lsm.batch.calculateBatch.domain.SettleDetail;

@Component
public class SettleDetailProcessor implements ItemProcessor<KeyAndCount, SettleDetail>, StepExecutionListener {

	private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	private StepExecution stepExecution;

	@Override
	public SettleDetail process(KeyAndCount item) throws Exception {
		Key key = item.key();
		ServicedPolicy servicedPolicy = ServicedPolicy.findById(key.serviceId());
		Long count = item.count();

		String targetDate = stepExecution.getJobParameters().getString("targetDate");

		return new SettleDetail(
			key.customerId(),
			key.serviceId(),
			count,
			servicedPolicy.getFee() * count,
			LocalDate.parse(targetDate, dateTimeFormatter)
		);
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
	}
}
