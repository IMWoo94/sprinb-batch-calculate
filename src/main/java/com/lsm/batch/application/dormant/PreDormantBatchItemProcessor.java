package com.lsm.batch.application.dormant;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.lsm.batch.customer.Customer;
import com.lsm.batch.dormantbatch.ItemProcessor;

@Component
public class PreDormantBatchItemProcessor implements ItemProcessor<Customer, Customer> {

	@Override
	public Customer process(Customer customer) {

		LocalDate targetDate = LocalDate.now()
			.minusDays(365)
			.plusDays(7);

		if (targetDate.equals(customer.getLoginAt().toLocalDate())) {
			return customer;
		} else {
			return null;
		}
	}
}
