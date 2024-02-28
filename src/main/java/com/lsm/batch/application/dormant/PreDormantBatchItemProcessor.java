package com.lsm.batch.application.dormant;

import org.springframework.stereotype.Component;

import com.lsm.batch.customer.Customer;
import com.lsm.batch.dormantbatch.ItemProcessor;

@Component
public class PreDormantBatchItemProcessor implements ItemProcessor<Customer, Customer> {

	@Override
	public Customer process(Customer item) {
		return null;
	}
}
