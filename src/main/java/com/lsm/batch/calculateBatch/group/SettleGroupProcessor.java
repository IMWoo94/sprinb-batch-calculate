package com.lsm.batch.calculateBatch.group;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.lsm.batch.calculateBatch.domain.Customer;
import com.lsm.batch.calculateBatch.domain.SettleGroup;
import com.lsm.batch.calculateBatch.domain.repository.SettleGroupRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SettleGroupProcessor implements ItemProcessor<Customer, List<SettleGroup>>, StepExecutionListener {

	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	private final SettleGroupRepository settleGroupRepository;
	private StepExecution stepExecution;

	@Override
	public List<SettleGroup> process(Customer item) throws Exception {
		String targetDate = stepExecution.getJobParameters().getString("targetDate");
		LocalDate end = LocalDate.parse(targetDate, formatter);

		return settleGroupRepository.findGroupByCustomerIdAndServiceId(
			end.minusDays(6),
			end,
			item.getId()
		);
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
	}
}
